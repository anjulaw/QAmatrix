package com.qamatrix.dto.response;

public class Response<T> {

	public static final int ERROR = -1;
	public static final int SUCCESS = 0;
	
	//Meta data
	public static final String META_TOTAL_PAGES = "pages";
	public static final String META_TOTAL_ELEMENTS = "total";
	
	private T data;
	private int status;
	
	

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	
}
