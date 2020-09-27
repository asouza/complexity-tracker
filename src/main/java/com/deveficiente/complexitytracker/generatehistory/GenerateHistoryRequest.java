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
	private String commitHashes;
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

	public GenerateHistoryRequest(@NotBlank String commitHashes,
			@NotBlank String projectId, @NotBlank String localGitPath,
			@NotBlank String javaFilesFolderPath) {
		super();
		this.commitHashes = commitHashes;
		this.projectId = projectId;
		this.localGitPath = localGitPath;
		this.javaFilesFolderPath = javaFilesFolderPath;
	}
	
	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}
	
	public void setEndDate(Calendar endDate) {
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
	
	public List<String> parseCommitsHashes(){
		String[] hashes = this.commitHashes.split(",");
		
		return Arrays.asList(hashes);
		
	}
	
	public CommitRange getCommitRange() {
		return Commits.betweenDates(startDate, endDate);
	}	

}
