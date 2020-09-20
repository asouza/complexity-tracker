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
		this.loc = result.getLoc();
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + ((hash == null) ? 0 : hash.hashCode());
		result = prime * result
				+ ((projectId == null) ? 0 : projectId.hashCode());
		return result;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ComplexityHistory other = (ComplexityHistory) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
		if (hash == null) {
			if (other.hash != null)
				return false;
		} else if (!hash.equals(other.hash))
			return false;
		if (projectId == null) {
			if (other.projectId != null)
				return false;
		} else if (!projectId.equals(other.projectId))
			return false;
		return true;
	}



	@Override
	public String toString() {
		return "ComplexityHistory [projectId=" + projectId + ", hash=" + hash
				+ ", commitDate=" + commitDate + ", className=" + className
				+ ", loc=" + loc + "]";
	}
	
	

}
