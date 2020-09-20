package com.deveficiente.complexitytracker;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.domain.Commit;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.NoPersistence;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.GitRepository;
import org.repodriller.scm.SCMRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.github.mauricioaniche.ck.CK;
import com.sun.tools.javac.util.List;

@SpringBootApplication
public class ComplexityTrackerApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ComplexityTrackerApplication.class, args);
		/*
		 * - preciso entrar num repositório
		 * 
		 * - ir para o primeiro commit - escolher um conjunto de classes -
		 * analisar o número de linhas de código das classes - vai para o último
		 * commit - analisa o número de linhas das classes de novo - armazena
		 * essas informações para plotar depois
		 * 
		 */
		
		// 6ce7a80199e2fde9a4eeae6f1793188e6c3fd007 (comeco)
		// f74be96dada91d6d15cc7c3954050e4133de16bf (primeiro release)
		// 6e0f2a66647a6471db1df01ac133435ef03dae66 (atual)
		/// Users/albertoluizsouza/ambiente/projetos/SSP
		
		new RepoDriller().start(() -> {
			new RepositoryMining()
					.in(GitRepository.singleProject(
							"/Users/albertoluizsouza/ambiente/projetos/SSP"))
					// ultimo commit da tag v3.0.0.M1

					.through(Commits.list(List.of(
							"6ce7a80199e2fde9a4eeae6f1793188e6c3fd007",
							"f74be96dada91d6d15cc7c3954050e4133de16bf",
							"6e0f2a66647a6471db1df01ac133435ef03dae66")))
					.process(new DevelopersVisitor(), new NoPersistence())
					.mine();
		});

	}

	static class DevelopersVisitor implements CommitVisitor {

		@Override
		public void process(SCMRepository repo, Commit commit,
				PersistenceMechanism writer) {

			System.out.println("===============process("+commit.getHash()+") ===================");
			System.out.println(commit.getHash());
			String folderToInspect = repo.getPath()
					+ "/src/main/java/org/jasig/ssp/service/impl";
			System.out.println("Folder to inpsect =>"+folderToInspect);
			
			try {
				Git gitSsp = Git.open(
						new File(repo.getPath()));
				gitSsp.checkout().setName(commit.getHash())
						.call();			

				CK ck = new CK(false, 0, false);
				ck.calculate(folderToInspect, result -> {
					System.out.println("Classe :" + result.getClassName());
					System.out.println("LOC :" + result.getLoc());
				});
			} catch (IOException | GitAPIException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
