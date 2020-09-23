package com.deveficiente.complexitytracker.reports;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.deveficiente.complexitytracker.generatehistory.ComplexityHistory;

@Controller
public class ReportController {

	@PersistenceContext
	private EntityManager manager;

	/*
	 * - Gráfico de barra - gerar relatório de métricas agrupado por classe
	 * entre commits
	 * 
	 * - Graficos de linha - gerar relatório de soma de loc do grupo analisado
	 * entre commits - gerar relatório de média de métrica do grupo analisado
	 * entre commits - gerar relatório combinando soma de métricas do grupo
	 * analisado - gerar relatório de média de combinação de métrica do grupo
	 * analisado entre commits
	 */

	@GetMapping(value = "/reports/pages/complexity-by-class")
	public String step1ShowPageComplexyByByClass(Model model,
			@RequestParam String projectId) {
		model.addAttribute("dataUrl",
				"/reports/data/complexity-by-class?projectId=ssp&className=org.jasig.ssp.service.impl.EarlyAlertServiceImpl");
		return "complexity-by-class";
	}

	@GetMapping(value = "/reports/data/complexity-by-class")
	@ResponseBody
	public ComplexityMetricPerClassBarData shoComplexyByByClass(
			@Valid SearchComplexityHistoryByClassRequest request) {
		List<ComplexityHistory> history = manager.createQuery(
				"select c from ComplexityHistory c "
				+ "where c.projectId=:projectId and "
				+ "c.className=:className "
				+ "order by c.commitDate asc",
				ComplexityHistory.class).
				setParameter("projectId", request.getProjectId())
				.setParameter("className", request.getClassName())
				.getResultList();
		

		return new ComplexityMetricPerClassBarData(history);
	}

}
