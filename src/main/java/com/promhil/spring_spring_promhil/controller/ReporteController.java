package com.promhil.spring_spring_promhil.controller;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.promhil.spring_spring_promhil.service.ReporteService;

import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;

@RestController
@RequestMapping("reporte")
public class ReporteController {

	@Autowired
	private ReporteService reporteService;

	@GetMapping("boleta")
	public void boletaReporte(@RequestParam Integer numBol, HttpServletResponse response) throws Exception {
		String reportPath = "/reporte/boleta.jrxml";

		Map<String, Object> params = new HashMap<>();
		params.put("pNumBoleta", numBol);
		
		JasperPrint jasperPrint = reporteService.getJasperPrint(params, reportPath);

		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", String.format("inline; filename=boleta-nro-%s.pdf", numBol));

		OutputStream outputStream = response.getOutputStream();
		JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

		outputStream.flush();
		outputStream.close();
	}
}
