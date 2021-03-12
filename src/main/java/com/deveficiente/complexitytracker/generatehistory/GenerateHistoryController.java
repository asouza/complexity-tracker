package com.deveficiente.complexitytracker.generatehistory;

import java.net.URI;
import java.util.List;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.scm.GitRemoteRepository;
import org.repodriller.scm.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;

/*
 * * Branch de código(if,else,loop,ternario,try,catch,switch)
 * * Acoplamento contextual. Acoplamento com classes especificas do projeto
 * * Funcao como argumento. 
 */

@Controller
public class GenerateHistoryController {

	@Autowired
	private EntityManager manager;
	@Autowired
	private TransactionTemplate tx;

	private static final Logger log = LoggerFactory
			.getLogger(GenerateHistoryController.class);

	@PostMapping(value = "/generate-history")
	@ResponseBody
	public ResponseEntity<?> generate(@Valid GenerateHistoryRequest request,
			UriComponentsBuilder uriComponent) {

		List<String> hashes = request.parseCommitsHashes();
		log.debug("Let's generate history for {} commits {} ", hashes.size(),
				hashes);

		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		new RepoDriller().start(() -> {
			new RepositoryMining()
					.in(GitRepository.singleProject(request.getLocalGitPath()))
					.through(request.getCommitRange())
					.process(
							new HistoryVisitor(request.getProjectId(),
									request.getJavaFilesFolderPath()),
							inMemoryWriter)
					.mine();
		});

		tx.executeWithoutResult(status -> {
			manager.createQuery(
					"delete from ComplexityHistory c where c.projectId = :projectId")
					.setParameter("projectId", request.getProjectId())
					.executeUpdate();
			inMemoryWriter.getHistory().forEach(manager::persist);
		});

		URI complexityHistoryGroupedReportUri = uriComponent.path(
				"/reports/pages/complexity-by-class?projectId={projectId}")
				.buildAndExpand(request.getProjectId()).toUri();
		return ResponseEntity.created(complexityHistoryGroupedReportUri)
				.build();

	}

	@PostMapping(value = "/generate-history/remote")
	@ResponseBody
	public String generateFromRemote(
			@Valid @RequestBody GenerateRemoteHistoryRequest request) {

		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		new RepoDriller().start(() -> {
			new RepositoryMining()
					.in(GitRemoteRepository.singleProject(request.getUrl()))
					.through(request.getCommitRange())
					.process(
							new HistoryVisitor(request.getProjectId(),
									request.getJavaFilesFolderPath()),
							inMemoryWriter)
					.mine();
		});

		tx.executeWithoutResult(status -> {
			manager.createQuery(
					"delete from ComplexityHistory c where c.projectId = :projectId")
					.setParameter("projectId", request.getProjectId())
					.executeUpdate();
			inMemoryWriter.getLastVersions().forEach(manager::persist);
		});

		return "/reports/pages/complexity-by-class?projectId="
				+ request.getProjectId();

	}

	@PostMapping(value = "/generate-history-class")
	@ResponseBody
	public ResponseEntity<?> generatePerClass(
			@Valid GenerateHistoryPerClassRequest request,
			UriComponentsBuilder uriComponent) {

		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		new RepoDriller().start(() -> {
			new RepositoryMining()
					.in(GitRepository.singleProject(request.getLocalGitPath()))
					.through(request.getCommitRange()).filters(commit -> {
						return commit.getModifications().stream()
								.anyMatch(modification -> {
									return modification.getFileName()
											.startsWith(request
													.getSimpleClassName());
								});
					})
					.process(
							new HistoryPerClassVisitor(request.getProjectId(),
									request.getSimpleClassName()),
							inMemoryWriter)
					.mine();
		});

		tx.executeWithoutResult(status -> {
			manager.createQuery(
					"delete from ComplexityHistory c where c.projectId = :projectId")
					.setParameter("projectId", request.getProjectId())
					.executeUpdate();
			inMemoryWriter.getHistory().forEach(manager::persist);
		});

		URI complexityHistoryGroupedReportUri = uriComponent.path(
				"/reports/pages/complexity-by-class?projectId={projectId}")
				.buildAndExpand(request.getProjectId()).toUri();
		return ResponseEntity.created(complexityHistoryGroupedReportUri)
				.build();

	}

}
