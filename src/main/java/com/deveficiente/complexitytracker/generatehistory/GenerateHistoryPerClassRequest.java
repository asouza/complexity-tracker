package com.deveficiente.complexitytracker.generatehistory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import org.repodriller.filter.range.CommitRange;
import org.repodriller.filter.range.Commits;

public class GenerateHistoryPerClassRequest {

	@NotBlank
	private String projectId;
	@NotBlank
	private String localGitPath;
	@NotBlank
	private String simpleClassName;
	@NotBlank
	private String startCommit;
	@NotBlank
	private String endCommit;

	/**
	 * 
	 * @param projectId id to associate with complexity generated data
	 * @param localGitPath path to local repo
	 * @param className name of class to be filtered. Not fully, just simple
	 */
	public GenerateHistoryPerClassRequest(
			@NotBlank String projectId, @NotBlank String localGitPath,
			@NotBlank String simpleClassName,@NotBlank String startCommit,@NotBlank String endCommit) {
		super();
		this.projectId = projectId;
		this.localGitPath = localGitPath;
		this.simpleClassName = simpleClassName;
		this.startCommit = startCommit;
		this.endCommit = endCommit;
	}
	
	public String getLocalGitPath() {
		return localGitPath;
	}

	public String getSimpleClassName() {
		return simpleClassName;
	}
	
	public String getProjectId() {
		return projectId;
	}

	public CommitRange getCommitRange() {
		return Commits.range(startCommit, endCommit);
	}
}
