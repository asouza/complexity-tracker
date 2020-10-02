package com.deveficiente.complexitytracker.generatehistory;

import javax.persistence.EntityManager;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class GenerateComplexyHistory {
	
	@Autowired
	private EntityManager manager;
	@Autowired
	private TransactionTemplate tx;	

	/**
	 * Persist history
	 * @param request necessary to extract project id
	 * @param history
	 */
	public void execute(@Valid HasProjectId hasProjectId,
			Iterable<ComplexityHistory> history) {
		// 1
		tx.executeWithoutResult(status -> {
			manager.createQuery(
					"delete from ComplexityHistory c where c.projectId = :projectId")
					.setParameter("projectId", hasProjectId.getProjectId())
					.executeUpdate();
			// 1
			history.forEach(manager::persist);
		});		
	}

}
