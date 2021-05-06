package com.deveficiente.complexitytracker.generatehistory;

import java.net.URI;

import javax.validation.Valid;

import org.repodriller.RepoDriller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
/*
 * Metric
 *  Branches - 1
 *  Contextual coupling - 1
 *  Function as argument - 1 
 */
public class GenerateHistoryController {

	//1
	@Autowired	
	private GenerateComplexyHistory generateComplexyHistory;
	

	@PostMapping(value = "/generate-history")
	// 1 : GenerateHistoryRequest - Contextual Coupling
	@ResponseBody
	public ResponseEntity<?> generate(@Valid GenerateHistoryRequest request,
			UriComponentsBuilder uriComponent) {

		// 1 : InMemoryComplexityHistoryWriter - Contextual Coupling
		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		
		//1 : function as an argument
		new RepoDriller().start(() -> {
			request.toMining(inMemoryWriter).mine();
		});
		
		generateComplexyHistory.execute(request,inMemoryWriter.getHistory());

		URI complexityHistoryGroupedReportUri = uriComponent.path(
				"/reports/pages/complexity-by-class?projectId={projectId}")
				.buildAndExpand(request.getProjectId()).toUri();
		return ResponseEntity.created(complexityHistoryGroupedReportUri)
				.build();

	}
	
	@PostMapping(value = "/generate-history-class")
	// 1 : GenerateHistoryPerClassRequest - Contextual Coupling
	@ResponseBody
	public ResponseEntity<?> generatePerClass(@Valid GenerateHistoryPerClassRequest request,
			UriComponentsBuilder uriComponent) {
		
		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		//1 : function as an argument
		new RepoDriller().start(() -> {
			request.toMining(inMemoryWriter).mine();
		});
		
		generateComplexyHistory.execute(request,inMemoryWriter.getHistory());
		
		URI complexityHistoryGroupedReportUri = uriComponent.path(
				"/reports/pages/complexity-by-class?projectId={projectId}")
				.buildAndExpand(request.getProjectId()).toUri();
		return ResponseEntity.created(complexityHistoryGroupedReportUri)
				.build();
		
	}

}
