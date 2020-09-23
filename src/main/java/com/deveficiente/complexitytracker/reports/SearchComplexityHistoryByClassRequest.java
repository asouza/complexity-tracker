package com.deveficiente.complexitytracker.reports;

import javax.validation.constraints.NotBlank;

public class SearchComplexityHistoryByClassRequest {

	@NotBlank
	private String projectId;
	@NotBlank
	private String className;

	public SearchComplexityHistoryByClassRequest(@NotBlank String projectId,
			@NotBlank String className) {
		super();
		this.projectId = projectId;
		this.className = className;
	}
	
	public String getProjectId() {
		return projectId;
	}
	
	public String getClassName() {
		return className;
	}

}
