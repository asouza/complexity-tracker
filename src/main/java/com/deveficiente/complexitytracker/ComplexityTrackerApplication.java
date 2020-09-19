package com.deveficiente.complexitytracker;

import org.repodriller.RepoDriller;
import org.repodriller.RepositoryMining;
import org.repodriller.domain.Commit;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.NoPersistence;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.persistence.csv.CSVFile;
import org.repodriller.scm.CommitVisitor;
import org.repodriller.scm.GitRepository;
import org.repodriller.scm.SCMRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
			new RepositoryMining().in(GitRepository.singleProject(
					"/Users/albertoluizsouza/ambiente/projetos/spring-framework"))
					// ultimo commit da tag v3.0.0.M1
					.through(Commits
							.single("dfcbc6f659b20d4f30929122ed3d2e28aa76360f"))
					.process(new DevelopersVisitor(), new NoPersistence())
					.mine();
		});
	}

	static class DevelopersVisitor implements CommitVisitor {

		@Override
		public void process(SCMRepository repo, Commit commit,
				PersistenceMechanism writer) {

			System.out.println("===============process ===================");
			System.out.println(repo.getPath());
			System.out.println(commit.getAuthor());

		}
	}

}
