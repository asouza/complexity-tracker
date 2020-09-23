package com.deveficiente.complexitytracker.generatehistory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import javax.validation.constraints.NotBlank;

import org.springframework.util.StringUtils;

public class GenerateHistoryRequest {

	@NotBlank
	private String commitHashes;
	@NotBlank
	private String projectId;
	@NotBlank
	private String localGitPath;
	@NotBlank
	private String javaFilesFolderPath;

	public GenerateHistoryRequest(@NotBlank String commitHashes,
			@NotBlank String projectId, @NotBlank String localGitPath,
			@NotBlank String javaFilesFolderPath) {
		super();
		this.commitHashes = commitHashes;
		this.projectId = projectId;
		this.localGitPath = localGitPath;
		this.javaFilesFolderPath = javaFilesFolderPath;
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

}
