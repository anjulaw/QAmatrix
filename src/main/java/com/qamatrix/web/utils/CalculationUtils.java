package com.qamatrix.web.utils;

import java.util.HashMap;
import java.util.Map;

import com.qamatrix.domain.Bug;
import com.qamatrix.domain.Issue;
import com.qamatrix.web.view.BugView;

public class CalculationUtils {

	public static BugView calInvalidBugRatio(Bug totalBugObject, Bug invalidBugObject){

        BugView bgView = new BugView();

        double ratio  = ((invalidBugObject.getTotal() / totalBugObject.getTotal()) * 100);

        bgView.setInvalidBugCountRatio(ratio);
        return bgView;
    }

    public static BugView caldefectRemovalEfficiency (Bug totaldefectQA, Bug totalDefectEndUser){

        BugView bugView = new BugView();

        double totalBugCount = totaldefectQA.getTotal() + totalDefectEndUser.getTotal();

        double defectRemovalEfficiencyRatio = ((totaldefectQA.getTotal() / totalBugCount) * 100 );

        bugView.setDefectRemovalEfficiency(defectRemovalEfficiencyRatio);
        return bugView;
    }

    public static BugView calDefectLeakage (Bug defectFoundUAT,Bug defectFoundQA){

        BugView bugView = new BugView();

        double defectLeakageRatio = ((defectFoundUAT.getTotal() / defectFoundQA.getTotal()) * 100);

        bugView.setDefectLeackageRatio(defectLeakageRatio);
        return bugView;

    }
    
    public static BugView calDefectSeverity(Bug validDefectSeverity,Bug totalValidDefects,String defectPriority){

        BugView bugView = new BugView();

        Map<String,Double> severityIndexMap = new HashMap<String,Double> ();

        severityIndexMap.put("Blocker",new Double(6.5));
        severityIndexMap.put("Critical",new Double(5.3));
        severityIndexMap.put("Medium",new Double(3.2));
        severityIndexMap.put("Low",new Double(2.0));

        if(severityIndexMap.containsKey(defectPriority)){
            double severityIndex = severityIndexMap.get(defectPriority);

            double defectSeverityIndexRatio = ((severityIndex * validDefectSeverity.getTotal())/totalValidDefects.getTotal());
            bugView.setDefectSeverityIndexRatio(defectSeverityIndexRatio);
        }

        return bugView;
    
    }
    
    public static BugView caleffortVariance(Issue issue){

        BugView bugView = new BugView();

        double estimatedEffort = issue.getFields().getAggregatetimeoriginalestimate()/3600;
        double spentEffort = issue.getFields().getAggregatetimespent()/3600;

        double effortVariance = ((spentEffort - estimatedEffort)/estimatedEffort) *100;
        bugView.setEffortVarianceRatio(effortVariance);

        return bugView;

    }
}
