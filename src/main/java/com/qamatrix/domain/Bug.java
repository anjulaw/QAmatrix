package com.qamatrix.domain;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Anjulaw on 2/13/2017.
 */
public class Bug implements Serializable {

    private double total;
    private List<Issue> issues;
    
	public List<Issue> getIssues() {
		return issues;
	}

	public void setIssues(List<Issue> issues) {
		this.issues = issues;
	}

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
