package com.deveficiente.complexitytracker.generatehistory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.repodriller.domain.Commit;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;
import org.springframework.util.Assert;

import com.github.mauricioaniche.ck.CK;

class HistoryVisitor implements CommitVisitor {

	private String projectId;
	
	private String customPathToInspect;
	private static final Logger log = LogManager
			.getLogger(HistoryVisitor.class);



	/**
	 * 
	 * @param projectId id to be assigned to history
	 * @param customPathToInspect path which should be analyzed inside the project. Ex: src/main/java/...
	 */
	public HistoryVisitor(String projectId,String customPathToInspect) {
		this.customPathToInspect = customPathToInspect;
		Assert.hasText(projectId,"Project id must not be empty");
		this.projectId = projectId;
	}

	@Override
	public void process(SCMRepository repo, Commit commit,
			PersistenceMechanism writer) {

		log.info("===============process({}) ===================",commit.getHash());
		System.out.println("===============process("+commit.getHash()+") ===================");
		String folderToInspect = repo.getPath() + "/"
				+ customPathToInspect;
		log.debug("Folder to inpsect {}",folderToInspect);
		
		try {
			Git gitSsp = Git.open(
					new File(repo.getPath()));
			gitSsp.checkout().setName(commit.getHash())
					.call();			

			CK ck = new CK(false, 0, false);
			ck.calculate(folderToInspect, result -> {
				//roda numa thread separada...
				ComplexityHistory newHistory = new ComplexityHistory(projectId,commit,result);
				writer.write(newHistory);
				log.debug("New history saved {}",newHistory);
			});
		} catch (IOException | GitAPIException e) {
			throw new RuntimeException(e);
		}

	}
}