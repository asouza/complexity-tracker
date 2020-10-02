package com.deveficiente.complexitytracker.generatehistory;

import java.util.Calendar;

import javax.validation.constraints.NotBlank;

import org.repodriller.RepositoryMining;
import org.repodriller.filter.range.Commits;
import org.repodriller.persistence.PersistenceMechanism;
import org.repodriller.scm.GitRepository;
import org.springframework.format.annotation.DateTimeFormat;

public class GenerateHistoryPerClassRequest implements HasProjectId {

	@NotBlank
	private String projectId;
	@NotBlank
	private String localGitPath;
	@NotBlank
	private String simpleClassName;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar endDate;

	public GenerateHistoryPerClassRequest(@NotBlank String projectId,
			@NotBlank String localGitPath, @NotBlank String simpleClassName,
			Calendar startDate, Calendar endDate) {
		super();
		this.projectId = projectId;
		this.localGitPath = localGitPath;
		this.simpleClassName = simpleClassName;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public RepositoryMining toMining(PersistenceMechanism persistence) {
		return new RepositoryMining()
				.in(GitRepository.singleProject(this.localGitPath))
				.through(Commits.betweenDates(startDate, endDate))
				// 1
				.filters(commit -> {
					// 1
					return commit.getModifications().stream()
							.anyMatch(modification -> {
								return modification.getFileName()
										.startsWith(this.simpleClassName);
							});
				})
				// 1
				.process(new HistoryPerClassVisitor(this.projectId,
						this.simpleClassName), persistence);

	}

	public String getProjectId() {
		return projectId;
	}

}
