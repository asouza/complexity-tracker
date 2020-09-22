package com.deveficiente.complexitytracker.reports;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ComplexyMetricPerClassBarItem {

	private List<String[]> labels = new ArrayList<>();
	private List<BigDecimal> values = new ArrayList<>();
	
	public ComplexyMetricPerClassBarItem() {
		labels.add(new String[] {"commit1"});
		//values.add(new BigDecimal[] {BigDecimal.ONE,BigDecimal.TEN});
	}
}
