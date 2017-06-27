package com.qamatrix.web.view;

import java.util.List;

public class ResultView<T> {
	
	private List<String> labels;
	
	private T data;
	
	private int status;

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
	

}
