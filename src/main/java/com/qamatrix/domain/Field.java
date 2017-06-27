package com.qamatrix.domain;

import java.io.Serializable;

public class Field implements Serializable {
	
	 private double aggregatetimespent;
	 private double aggregatetimeoriginalestimate;
	 
	public double getAggregatetimespent() {
		return aggregatetimespent;
	}
	public void setAggregatetimespent(double aggregatetimespent) {
		this.aggregatetimespent = aggregatetimespent;
	}
	public double getAggregatetimeoriginalestimate() {
		return aggregatetimeoriginalestimate;
	}
	public void setAggregatetimeoriginalestimate(double aggregatetimeoriginalestimate) {
		this.aggregatetimeoriginalestimate = aggregatetimeoriginalestimate;
	}
	 
	 

}
