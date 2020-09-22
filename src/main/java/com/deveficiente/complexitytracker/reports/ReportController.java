package com.deveficiente.complexitytracker.reports;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReportController {

	/*  	
	 * - Gráfico de barra
	 *  - gerar relatório de métricas agrupado por classe entre commits
	 *  
	 *  - Graficos de linha
	 *  - gerar relatório de soma de loc do grupo analisado entre commits
	 *  - gerar relatório de média de métrica do grupo analisado entre commits
	 *  - gerar relatório combinando soma de métricas do grupo analisado
	 *  - gerar relatório de média de combinação de métrica do grupo analisado entre commits
	 */
	
	@GetMapping(value = "/reports/pages/complexity-by-class")
	public String shoComplexyByByClass(Model model,@RequestParam String projectId) {
		model.addAttribute("dataUrl","/reports/data/complexity-by-class?projectId="+projectId);
		return "complexity-by-class";
	}
	
//	@GetMapping(value = "/reports/data/complexity-by-class")
//	@ResponseBody
//	public List<ComplexyMetricPerClassBarItem> shoComplexyByByClass(@RequestParam String projectId) {
//		
//		return "complexity-by-class";
//	}

}
