package com.deveficiente.complexitytracker;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.filter.range.Commits;
import org.repodriller.scm.GitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GenerateHistoryController {

	@Autowired
	private EntityManager manager;
	@Autowired
	private TransactionTemplate tx;

	@PostMapping(value = "/generate-history")
	public String generate(@Valid GenerateHistoryRequest request) {
		/*
		 * List.of(
							"6ce7a80199e2fde9a4eeae6f1793188e6c3fd007",
							"f74be96dada91d6d15cc7c3954050e4133de16bf",
							"6e0f2a66647a6471db1df01ac133435ef03dae66")
		 */		
		System.out.println("generate");
		InMemoryComplexityHistoryWriter inMemoryWriter = new InMemoryComplexityHistoryWriter();
		new RepoDriller().start(() -> {
			List<String> hashes = request.parseCommitsHashes();
			new RepositoryMining()
					.in(GitRepository.singleProject(
							request.getLocalGitPath()))

					.through(Commits.list(hashes))
					.process(new HistoryVisitor(request.getProjectId()),
							inMemoryWriter)
					.mine();
		});

		tx.executeWithoutResult(status -> {
			inMemoryWriter.getHistory().forEach(manager::persist);
		});
		
		return "redirect:/..."; 

	}

}
