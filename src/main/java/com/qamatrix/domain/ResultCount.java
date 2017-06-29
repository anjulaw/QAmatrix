package com.qamatrix.domain;

/**
 * Created by Anjula on 6/23/2017.
 */
public class ResultCount {

    private double invalidBugCount;
    private double defectQABugCount;
    private double defectEndUserBugCount;
    private double defectsFoundUATCount;
    private double defectsFoundQACount;
    private double defectWithSeverity;
    private double totalDefects;


    public double getDefectWithSeverity() {
        return defectWithSeverity;
    }

    public void setDefectWithSeverity(double defectWithSeverity) {
        this.defectWithSeverity = defectWithSeverity;
    }

    public double getTotalDefects() {
        return totalDefects;
    }

    public void setTotalDefects(double totalDefects) {
        this.totalDefects = totalDefects;
    }

    public double getDefectsFoundUATCount() {
        return defectsFoundUATCount;
    }

    public void setDefectsFoundUATCount(double defectsFoundUATCount) {
        this.defectsFoundUATCount = defectsFoundUATCount;
    }

    public double getDefectsFoundQACount() {
        return defectsFoundQACount;
    }

    public void setDefectsFoundQACount(double defectsFoundQACount) {
        this.defectsFoundQACount = defectsFoundQACount;
    }

    public double getDefectQABugCount() {
        return defectQABugCount;
    }

    public void setDefectQABugCount(double defectQABugCount) {
        this.defectQABugCount = defectQABugCount;
    }

    public double getDefectEndUserBugCount() {
        return defectEndUserBugCount;
    }

    public void setDefectEndUserBugCount(double defectEndUserBugCount) {
        this.defectEndUserBugCount = defectEndUserBugCount;
    }

    public double getInvalidBugCount() {
        return invalidBugCount;
    }

    public void setInvalidBugCount(double invalidBugCount) {
        this.invalidBugCount = invalidBugCount;
    }




}
