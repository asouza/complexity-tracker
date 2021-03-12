package com.deveficiente.complexitytracker.generatehistory;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;
import org.repodriller.filter.range.CommitRange;
import org.repodriller.filter.range.Commits;
import org.springframework.format.annotation.DateTimeFormat;

public class GenerateRemoteHistoryRequest {

	@NotBlank
	private String projectId;
	@NotBlank
	@URL
	private String url;
	@NotBlank
	private String javaFilesFolderPath;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar startDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Calendar endDate;

	public GenerateRemoteHistoryRequest(
			@NotBlank String projectId, @NotBlank @URL String url,
			@NotBlank String javaFilesFolderPath) {
		super();
		this.projectId = projectId;
		this.url = url;
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
	
	public String getUrl() {
		return url;
	}
	
	public String getProjectId() {
		return projectId;
	}
	
	public CommitRange getCommitRange() {
		return Commits.betweenDates(startDate, endDate);
	}	

}
