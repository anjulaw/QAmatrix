package com.qamatrix.domain;

import java.io.Serializable;

/**
 * Created by Anjulaw on 12/26/2016.
 */
public class Issue implements Serializable {

    String expand;
    int id;
    String key;
    Field fields;
    
    public Field getFields() {
		return fields;
	}

	public void setFields(Field fields) {
		this.fields = fields;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
