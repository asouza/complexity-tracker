package com.deveficiente.complexitytracker;

import java.util.Collection;
import java.util.LinkedHashSet;

import org.repodriller.persistence.PersistenceMechanism;
import org.springframework.util.Assert;

public class InMemoryComplexityHistoryWriter implements PersistenceMechanism {
	private Collection<ComplexityHistory> history = new LinkedHashSet<>();

	@Override
	public void write(Object... line) {
		Assert.notNull(line,"Line must not be null");
		Assert.isTrue(line.length == 1,"Line must have just one ComplexityHistory");
		Assert.isTrue(line[0] instanceof ComplexityHistory,"Object of a line must be instance of ComplexityHistory");
		
		ComplexityHistory newComplexityHistory = (ComplexityHistory) line[0];
		history.add(newComplexityHistory);
		
	}
	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	public Iterable<ComplexityHistory> getHistory() {
		return this.history;
	}

}
