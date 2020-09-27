package com.deveficiente.complexitytracker.reports;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.deveficiente.complexitytracker.generatehistory.ComplexityHistory;

//9
public class ComplexityMetricPerClassBarData {

	private LinkedHashSet<String> labels = new LinkedHashSet<>();
	//1
	private List<BarGroupedDataSet> datasets = new ArrayList<>();

	public ComplexityMetricPerClassBarData(List<ComplexityHistory> history) {
		this.labels.addAll(history.stream()
				//1
				.map(ComplexityHistory::getCommitDate)
				//1
				.map(date -> date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm")))
				//1
				.collect(Collectors.toCollection(() -> new LinkedHashSet<>())));
		
		List<Integer> cbos = history.stream()
				//1
			.map(ComplexityHistory :: getCbo)
			.collect(Collectors.toList());
		//1
		BarGroupedDataSet groupedCbos = new BarGroupedDataSet(cbos,"CBO","blue");
		this.datasets.add(groupedCbos);
		
		List<Integer> wmcs = history.stream()
				//1
				.map(ComplexityHistory :: getWmc)
				.collect(Collectors.toList());
		
		BarGroupedDataSet groupedWmcs = new BarGroupedDataSet(wmcs,"WMC","green");
		this.datasets.add(groupedWmcs);
		
		List<Integer> lcoms = history.stream()
				//1
				.map(ComplexityHistory :: getLcom)
				.collect(Collectors.toList());
		
		BarGroupedDataSet groupedLcoms = new BarGroupedDataSet(lcoms,"LCOM","yellow");
		this.datasets.add(groupedLcoms);
		
		List<Integer> loc = history.stream()
				//1
				.map(ComplexityHistory :: getLoc)
				.collect(Collectors.toList());
		
		BarGroupedDataSet groupedLoc = new BarGroupedDataSet(loc,"LOC","red");
		this.datasets.add(groupedLoc);
		
	}
	
	public LinkedHashSet<String> getLabels() {
		return labels;
	}

	public List<BarGroupedDataSet> getDatasets() {
		return datasets;
	}
}
