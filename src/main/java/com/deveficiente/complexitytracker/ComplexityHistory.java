package com.deveficiente.complexitytracker;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.repodriller.domain.Commit;

import com.github.mauricioaniche.ck.CKClassResult;

@Entity
public class ComplexityHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String projectId;
	private String hash;
	private LocalDateTime commitDate;
	private String className;
	private int loc;

	public ComplexityHistory(String projectId, Commit commit,
			CKClassResult result) {
		this.projectId = projectId;
		this.hash = commit.getHash();
		this.commitDate = LocalDateTime.ofInstant(commit.getDate().toInstant(),
				ZoneId.systemDefault());
		this.className = result.getClassName();
		loc = result.getLoc();
	}

}
