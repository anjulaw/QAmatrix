package com.qamatrix.domain;

public class User {

	   /* @Id*/
	    int id;
	    String email;

	    public User() {
	    }

	    public int getId() {
	        return id;
	    }

	    public void setId(int id) {
	        this.id = id;
	    }

	    public String getName() {
	        return email;
	    }

	    public void setName(String name) {
	        this.email = name;
	    }
	}

