package com.deveficiente.complexitytracker.reports;

import java.util.List;

public class BarGroupedDataSet {

	private List<? extends Number> data;
	private String label;
	private String backgroundColor;

	public BarGroupedDataSet(List<? extends Number> values, String label,
			String backgroundColor) {
		this.data = values;
		this.label = label;
		this.backgroundColor = backgroundColor;
	}

	public List<? extends Number> getData() {
		return data;
	}

	public String getLabel() {
		return label;
	}

	public String getBackgroundColor() {
		return backgroundColor;
	}

}
