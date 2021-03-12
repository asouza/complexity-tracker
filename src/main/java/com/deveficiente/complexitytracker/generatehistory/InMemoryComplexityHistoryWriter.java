package com.deveficiente.complexitytracker.generatehistory;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

import org.repodriller.persistence.PersistenceMechanism;
import org.springframework.util.Assert;

public class InMemoryComplexityHistoryWriter implements PersistenceMechanism {
	private Collection<ComplexityHistory> history = new LinkedHashSet<>();

	@Override
	public void write(Object... line) {
		Assert.notNull(line, "Line must not be null");
		Assert.isTrue(line.length == 1,
				"Line must have just one ComplexityHistory");
		Assert.isTrue(line[0] instanceof ComplexityHistory,
				"Object of a line must be instance of ComplexityHistory");

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

	public Iterable<ComplexityHistory> getLastVersions() {
		/*
		 * verifico se tem no mapa momento de commit é maior, troca
		 */
		HashMap<String, ComplexityHistory> hashMap = new HashMap<>();
		for (ComplexityHistory currentComplexity : history) {
			System.out.println(currentComplexity);
			String className = currentComplexity.getClassName();
			hashMap.computeIfAbsent(className, key -> {
				return currentComplexity;
			});

			ComplexityHistory earlier = hashMap.get(className);
			if (currentComplexity.wasCommitedAfter(earlier)) {
				hashMap.put(className, currentComplexity);
			}
		}
		
		System.out.println("<<<<<=========>>>>>>");
		
		hashMap.values().forEach(System.out :: println);

		return hashMap.values();
	}

}
