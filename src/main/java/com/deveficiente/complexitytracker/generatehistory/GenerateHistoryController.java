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
//13
//9
//6
public class GenerateHistoryController {

	//1
	@Autowired	
	private GenerateComplexyHistory generateComplexyHistory;
	

	@PostMapping(value = "/generate-history")
	// 1
	@ResponseBody
	public ResponseEntity<?> generate(@Valid GenerateHistoryRequest request,
			UriComponentsBuilder uriComponent) {

		// 1
		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		
		//1
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
	// 1
	@ResponseBody
	public ResponseEntity<?> generatePerClass(@Valid GenerateHistoryPerClassRequest request,
			UriComponentsBuilder uriComponent) {
		
		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		// 1
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
