package com.deveficiente.complexitytracker.generatehistory;

import java.net.URI;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.Study;
import org.repodriller.scm.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
//13
//9
public class GenerateHistoryController {

	@Autowired
	private EntityManager manager;
	@Autowired
	private TransactionTemplate tx;

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

		// 1
		tx.executeWithoutResult(status -> {
			manager.createQuery(
					"delete from ComplexityHistory c where c.projectId = :projectId")
					.setParameter("projectId", request.getProjectId())
					.executeUpdate();
			// 1
			inMemoryWriter.getHistory().forEach(manager::persist);
		});

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
		
		// 1
		tx.executeWithoutResult(status -> {
			manager.createQuery(
					"delete from ComplexityHistory c where c.projectId = :projectId")
			.setParameter("projectId", request.getProjectId())
			.executeUpdate();
			// 1
			inMemoryWriter.getHistory().forEach(manager::persist);
		});
		
		URI complexityHistoryGroupedReportUri = uriComponent.path(
				"/reports/pages/complexity-by-class?projectId={projectId}")
				.buildAndExpand(request.getProjectId()).toUri();
		return ResponseEntity.created(complexityHistoryGroupedReportUri)
				.build();
		
	}

}
