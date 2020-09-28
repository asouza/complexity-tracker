package com.deveficiente.complexitytracker.generatehistory;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
//6
public class GenerateHistoryController {

	@Autowired
	private EntityManager manager;
	@Autowired
	private TransactionTemplate tx;

	private static final Logger log = LoggerFactory
			.getLogger(GenerateHistoryController.class);

	@PostMapping(value = "/generate-history")
	// 1
	@ResponseBody
	public ResponseEntity<?> generate(@Valid @RequestBody GenerateHistoryRequest request,
			UriComponentsBuilder uriComponent) {

		List<String> hashes = request.parseCommitsHashes();
		log.debug("Let's generate history for {} commits {} ", hashes.size(),
				hashes);

		// 1
		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		// 1
		new RepoDriller().start(() -> {
			new RepositoryMining()
					.in(GitRepository.singleProject(request.getLocalGitPath()))
					.through(Commits.list(hashes))
					// 1
					.process(new HistoryVisitor(request.getProjectId(),request.getJavaFilesFolderPath()),
							inMemoryWriter)
					.mine();
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
