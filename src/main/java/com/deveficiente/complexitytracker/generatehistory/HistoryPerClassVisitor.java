package com.deveficiente.complexitytracker.generatehistory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.apache.log4j.spi.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.repodriller.domain.Commit;
import org.repodriller.domain.Modification;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.SCMRepository;
import org.springframework.util.Assert;

import com.github.mauricioaniche.ck.CK;

/**
 * 
 * 
 * 
 * 
 *  * Branch de código (if,else,loop,switch,ternario
 *  					   ,try,catch)
 *  * Acoplamento contextual. Este é o acoplamento com classes específicas do projeto
 * 	* Passagem de função como argumento. 
 * 
 *
 */

class HistoryPerClassVisitor implements CommitVisitor {

	private String projectId;
	
	private static final Logger log = LogManager
			.getLogger(HistoryPerClassVisitor.class);

	private String simpleClassName;



	/**
	 * 
	 * @param projectId id to be assigned to history
	 */
	public HistoryPerClassVisitor(String projectId,String simpleClassName) {
		Assert.hasText(projectId,"Project id must not be empty");
		Assert.hasText(simpleClassName,"classname must not be empty");
		this.simpleClassName = simpleClassName;
		this.projectId = projectId;
	}

	@Override
	public void process(SCMRepository repo, Commit commit,
			PersistenceMechanism writer) {

		log.info("===============process({}) ===================",commit.getHash());
		System.out.println("===============process("+commit.getHash()+") ===================");
		
		try {
			Git gitSsp = Git.open(
					new File(repo.getPath()));
			gitSsp.checkout().setName(commit.getHash())
					.call();	
			
			Optional<Modification> modificationToTargetClass = commit
					.getModifications()
					.stream()
					.filter(modification -> {
				return modification.getFileName().startsWith(simpleClassName);
			}).findFirst();

			if(modificationToTargetClass.isPresent()) {
				String pathToFile = repo.getPath()+"/"+modificationToTargetClass.get().getNewPath();
				log.debug("Analyzing {}",pathToFile);
				System.out.println("Analyzing "+pathToFile);
				
				
				CK ck = new CK(false, 0, false);
				ck.calculate(pathToFile, result -> {
					ComplexityHistory newHistory = new ComplexityHistory(projectId,commit,result);
					writer.write(newHistory);
					log.debug("New history saved {}",newHistory);
				});				
			}
			
		} catch (IOException | GitAPIException e) {
			throw new RuntimeException(e);
		}

	}
}