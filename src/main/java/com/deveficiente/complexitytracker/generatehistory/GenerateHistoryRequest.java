package com.deveficiente.complexitytracker.generatehistory;

import java.util.Calendar;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import org.repodriller.RepositoryMining;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.GitRepository;
import org.springframework.format.annotation.DateTimeFormat;

public class GenerateHistoryRequest implements HasProjectId {

	@NotBlank
	private String projectId;
	@NotBlank
	private String localGitPath;
	@NotBlank
	private String javaFilesFolderPath;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar endDate;

	public GenerateHistoryRequest(@NotBlank String projectId,
			@NotBlank String localGitPath, @NotBlank String javaFilesFolderPath,
			Calendar startDate, Calendar endDate) {
		super();
		this.projectId = projectId;
		this.localGitPath = localGitPath;
		this.javaFilesFolderPath = javaFilesFolderPath;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public RepositoryMining toMining(PersistenceMechanism persistence) {
		return new RepositoryMining()
		.in(GitRepository.singleProject(this.localGitPath))
		.visitorsAreThreadSafe(true)
		.visitorsChangeRepoState(true)
		.through(Commits.betweenDates(startDate, endDate))
		.withThreads()
		// 1
		.process(new HistoryVisitor(this.projectId,this.javaFilesFolderPath),
				persistence);

	}

	public String getProjectId() {
		return getProjectId();
	}

}
