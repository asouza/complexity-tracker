package com.deveficiente.complexitytracker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.domain.Commit;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.NoPersistence;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.GitRepository;
import org.repodriller.scm.SCMRepository;
import org.repodriller.util.RDFileUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.tmatesoft.sqljet.core.internal.lang.SqlParser.delete_stmt_return;

import com.github.mauricioaniche.ck.CK;

@SpringBootApplication
public class ComplexityTrackerApplication {

	public static void main(String[] args) {
		// SpringApplication.run(ComplexityTrackerApplication.class, args);
		/*
		 * - preciso entrar num repositório
		 * 
		 * - ir para o primeiro commit - escolher um conjunto de classes - se
		 * for possível por um critério como classe mais refatorada - voltar
		 * para o primeiro commit - analisar o número de linhas de código das
		 * classes - vai para o último commit - analisa o número de linhas das
		 * classes de novo - armazena essas informações para plotar depois
		 * 
		 */

		new RepoDriller().start(() -> {
			new RepositoryMining()
					.in(GitRepository.singleProject("/Users/albertoluizsouza/ambiente/projetos/SSP"))
					// ultimo commit da tag v3.0.0.M1
					.through(Commits
							.single("f74be96dada91d6d15cc7c3954050e4133de16bf"))
					.process(new DevelopersVisitor(), new NoPersistence())
					.mine();
		});
		
	}

	static class DevelopersVisitor implements CommitVisitor {

		@Override
		public void process(SCMRepository repo, Commit commit,
				PersistenceMechanism writer) {

			System.out.println("===============process ===================");
			System.out.println(repo.getPath()
					+ "/src/main/java/org/jasig/ssp/service/impl");
			String folderToInspect = repo.getPath()
					+ "/src/main/java/org/jasig/ssp/service/impl";

			CK ck = new CK(false, 0, false);
			ck.calculate(folderToInspect, result -> {
				System.out.println("Classe :" + result.getClassName());
				System.out.println("LOC :" + result.getLoc());
			});
			
		}
	}

}
