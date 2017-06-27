package com.qamatrix.web.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anjulaw on 12/26/2016.
 */
public class IssueView implements Serializable {

    int id;
    int totalBugCount;
    List<String> months =new ArrayList<String>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotalBugCount() {
        return totalBugCount;
    }

    public void setTotalBugCount(int totalBugCount) {
        this.totalBugCount = totalBugCount;
    }

    public List<String> getMonths() {
        return months;
    }

    public void setMonths(List<String> months) {
        this.months = months;
    }
}
