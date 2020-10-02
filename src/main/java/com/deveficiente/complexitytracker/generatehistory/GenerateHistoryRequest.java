package com.deveficiente.complexitytracker.generatehistory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.repodriller.filter.range.CommitRange;
import org.repodriller.filter.range.Commits;
import org.springframework.format.annotation.DateTimeFormat;

public class GenerateHistoryRequest {

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

	public String getJavaFilesFolderPath() {
		return javaFilesFolderPath;
	}

	public String getLocalGitPath() {
		return localGitPath;
	}

	public String getProjectId() {
		return projectId;
	}

	public CommitRange getCommitRange() {
		return Commits.betweenDates(startDate, endDate);
	}

}
