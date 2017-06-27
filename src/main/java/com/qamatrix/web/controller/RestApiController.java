package com.qamatrix.web.controller;


import com.qamatrix.domain.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.qamatrix.dto.response.Response;
import com.qamatrix.services.data.IDataService;
import com.qamatrix.utils.WebConstants;
import com.qamatrix.web.utils.CalculationUtils;
import com.qamatrix.web.view.BugView;
import com.qamatrix.web.view.ResultView;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Anjulaw on 12/26/2016.
 */
@Controller
@RequestMapping("/v1")
public class RestApiController {

    @Autowired
    Environment env;

    @Autowired
    RestTemplate resTemplate;

    @Autowired
    HttpEntity qaMetrixHttpEntity;
    
    @Autowired
	IDataService dataService;
    
    @RequestMapping(value = "/reporters", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public Response<List<JiraUser>> getReporters( @RequestParam("username") String username ){
    	
    	Response<List<JiraUser>> response = new Response<>();
    	List<JiraUser> resultList = new ArrayList<>();
    	
    	if(username!=null){
    		
    		username = username.substring(username.lastIndexOf(",")+1,username.length());
    	}
		
    	
    	List<JiraUser> jiraUserList = dataService.getJiraUsers();
    	if(!CollectionUtils.isEmpty(jiraUserList)){
    		
    		for (JiraUser jiraUser : jiraUserList) {
				
    			if(jiraUser.getDisplayName()!=null && jiraUser.getDisplayName().toLowerCase().startsWith(username.toLowerCase())){
    				
    				resultList.add(jiraUser);
    			}
			}
    		
    		response.setData(resultList);
    		response.setStatus( Response.SUCCESS );
    	}
    	else{
    		
    		response.setData(resultList);
    		response.setStatus( Response.ERROR );
    	}
    	
    	return response;
    	
    }
    
    @RequestMapping(value = "/projects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
	public Response<List<Project>> getProject( @RequestParam("project") String project ){
    	
    	Response<List<Project>> response = new Response<>();
    	List<Project> resultList = new ArrayList<>();
    	
    	if(project!=null){
    		
    		project = project.substring(project.lastIndexOf(",")+1,project.length());
    	}
		
    	
    	List<Project> jiraProjectList = dataService.getProjects();
    	if(!CollectionUtils.isEmpty(jiraProjectList)){
    		
    		for (Project jiraProject : jiraProjectList) {
				
    			if(jiraProject.getName()!=null && jiraProject.getName().toLowerCase().startsWith(project.toLowerCase())){
    				
    				resultList.add(jiraProject);
    			}
			}
    		
    		response.setData(resultList);
    		response.setStatus( Response.SUCCESS );
    	}
    	else{
    		
    		response.setData(resultList);
    		response.setStatus( Response.ERROR );
    	}
    	
    	return response;
    	
    }

    @RequestMapping(value = "/getInvalidDefects", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResultView<BugView> invalidDefects(@RequestParam(name = "fromDate") String fromDate, @RequestParam(name = "toDate") String toDate,  @RequestParam(name = "reporter") String reporter) {

    	ResultView resultView = new ResultView<>();
    	
        try {
        	
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
        	Date startDate = sdf.parse(fromDate);        	
        	Date endDate = sdf.parse(toDate);
        	
        	Calendar startCalendar = new GregorianCalendar();
        	startCalendar.setTime(startDate);
        	Calendar endCalendar = new GregorianCalendar();
        	endCalendar.setTime(endDate);
        	
        	int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        	int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        	
        	List<String> dateList = new ArrayList<>();        	
        	List<String> lables = new ArrayList<>();
        	SimpleDateFormat jiraDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	
        	
        	String date = jiraDateFormat.format(startDate);
        	dateList.add(date);
        	
        	Date initialDate = startDate;
        	for (int i = 0; i < diffMonth+1; i++) {
        		
        		lables.add(jiraDateFormat.format(initialDate));
				
        		Calendar c = Calendar.getInstance();
        		c.setTime(initialDate);
        		c.add(Calendar.MONTH, 1);  // number of days to add
        		
        		initialDate = c.getTime();
        		String datex = jiraDateFormat.format(initialDate);
        		dateList.add(datex);
        		
        		
			}
        	
        	List<BugView> bugVierwList = new ArrayList<>();


        	for (int i = 0; i < dateList.size()-1; i++) {
				
        		String totalBugCountUrl = "https://codegen.atlassian.net/rest/api/2/search?jql=createdDate >= "+dateList.get(i)+" AND createdDate <= "+dateList.get(i+1)+" AND reporter in (\""+reporter+"\")AND type = \"Local Issue\"&fields=key&maxResults=100";
        		System.out.println(totalBugCountUrl);
        		
        		String invalidBugCountUrl = "https://codegen.atlassian.net/rest/api/2/search?jql=createdDate >= "+dateList.get(i)+" AND createdDate <= "+dateList.get(i+1)+" AND reporter in (\""+reporter+"\") AND type = \"Local Issue\" AND resolution in (Invalid)&fields=key&maxResults=100";
				System.out.println("Invalid Bug Count url "+invalidBugCountUrl);

        		ResponseEntity<Bug> totalBugCountResponse = resTemplate.exchange(totalBugCountUrl, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
                Bug bugCountObject = totalBugCountResponse.getBody();
                if (bugCountObject != null) {

                    ResponseEntity<Bug> invalidBugCountResponse = resTemplate.exchange(invalidBugCountUrl, HttpMethod.GET, qaMetrixHttpEntity, Bug.class);
                    Bug bugCountInvalid = invalidBugCountResponse.getBody();

                    BugView bugViewinvalidDefects = CalculationUtils.calInvalidBugRatio(bugCountObject, bugCountInvalid);

					String lable = dateList.get(i);
					bugViewinvalidDefects.setLables(lable);

					ResultCount resultCount = new ResultCount();
					resultCount.setInvalidBugCount(bugCountInvalid.getTotal());
					bugViewinvalidDefects.setResultCount(resultCount);

					bugVierwList.add(bugViewinvalidDefects);

                }
        		
			}

        	resultView.setStatus(WebConstants.SUCCESS);
        	resultView.setData(bugVierwList);
        	resultView.setLabels(lables);
        	
        	
        } catch (Exception e) {

        	resultView.setStatus(WebConstants.ERROR);
        	
            e.printStackTrace();
        }

        return resultView;

    }


    @RequestMapping(value = "/getDefectRemoval", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResultView<BugView> defectRemoval (@RequestParam(name = "fromDate") String fromDate, @RequestParam(name = "toDate") String toDate, @RequestParam(name = "project") String project) {
    	
    	ResultView resultView = new ResultView<>();
    	
        try{
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
        	Date startDate = sdf.parse(fromDate);        	
        	Date endDate = sdf.parse(toDate);
        	
        	Calendar startCalendar = new GregorianCalendar();
        	startCalendar.setTime(startDate);
        	Calendar endCalendar = new GregorianCalendar();
        	endCalendar.setTime(endDate);
        	
        	int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        	int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        	
        	List<String> dateList = new ArrayList<>();        	
        	List<String> lables = new ArrayList<>();
        	SimpleDateFormat jiraDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	
        	
        	String date = jiraDateFormat.format(startDate);
        	dateList.add(date);
        	
        	Date initialDate = startDate;
        	for (int i = 0; i < diffMonth+1; i++) {
        		
        		lables.add(jiraDateFormat.format(initialDate));
				
        		Calendar c = Calendar.getInstance();
        		c.setTime(initialDate);
        		c.add(Calendar.MONTH, 1);  // number of days to add
        		
        		initialDate = c.getTime();
        		String datex = jiraDateFormat.format(initialDate);
        		dateList.add(datex);
        		
        	}
        	
        	List<BugView> bugVierwList = new ArrayList<>();
//			List<Double> defectQAList = new ArrayList<>();
//			List<Double> defectEndUserList = new ArrayList<>();
        	
        	for (int i = 0; i < dateList.size()-1; i++){
        		
        		final String defectQATestUrl = "https://codegen.atlassian.net/rest/api/2/search?jql=\"Project Ref\" in ("+project+") AND issuetype in (\"Local Issue\") AND createdDate >= "+dateList.get(i)+" AND createdDate <= "+dateList.get(i+1)+"&fields=key&maxResults=100";
				System.out.println("defectQATestUrl "+defectQATestUrl);

				final String defectEndUserUrl = "https://codegen.atlassian.net/rest/api/2/search?jql=createdDate >= "+dateList.get(i)+" AND createdDate <= "+dateList.get(i+1)+" AND  project  = "+project+" AND level = EXTERNAL AND type in (\"Production Issue\",\"Non-Prod Issue\")&fields=key&maxResults=100";
				System.out.println("defectEndUserUrl "+defectEndUserUrl);

                ResponseEntity<Bug> defectQAResponse = resTemplate.exchange(defectQATestUrl, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
                Bug defectQAObject = defectQAResponse.getBody();
				//defectQAList.add(defectQAObject.getTotal());

                if (defectQAObject !=null){

                    ResponseEntity<Bug> defectEndUserResponse = resTemplate.exchange(defectEndUserUrl, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
                    Bug defectEndUserObject = defectEndUserResponse.getBody();
					//defectEndUserList.add(defectEndUserObject.getTotal());

                    BugView bugViewdefectRemoval = CalculationUtils.caldefectRemovalEfficiency(defectQAObject,defectEndUserObject);

					String label = dateList.get(i);
					bugViewdefectRemoval.setLables(label);

					ResultCount resultCount = new ResultCount();
					resultCount.setDefectQABugCount(defectQAObject.getTotal());
					resultCount.setDefectEndUserBugCount(defectEndUserObject.getTotal());

					bugViewdefectRemoval.setResultCount(resultCount);

                    bugVierwList.add(bugViewdefectRemoval);

                }
        		
        	}
        	
        	for (BugView bugView : bugVierwList) {
				System.out.println(bugView.getDefectRemovalEfficiency());
			}

			/*for (Double defectQA : defectQAList){
				System.out.println(defectQA);
			}

			for (Double defectEndUser : defectEndUserList){
				System.out.println(defectEndUser);
			}*/
        	
        	resultView.setStatus(WebConstants.SUCCESS);
        	resultView.setData(bugVierwList);
        	resultView.setLabels(lables);
        	
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultView;
    }

    @RequestMapping("/getDefectLeakage")
    public BugView defectLeakage(@RequestParam(name="issuetype") String issuetype){
        try{

            final String defectsFoundUAT = "https://codegen.atlassian.net/rest/api/2/search?jql=createdDate >= 2017-01-01 AND createdDate <= 2017-01-31 AND  project  = \"Tour America\" AND level = EXTERNAL AND type in (\"Non-Prod Issue\")";

            final String defectsFoundQA = "https://codegen.atlassian.net/rest/api/2/search?jql=\"Project Ref\" in (TA) AND issuetype in (\"Local Issue\") AND createdDate >= 2017-01-01 AND createdDate <= 2017-01-31";

            ResponseEntity<Bug> defectFoundUATResponse = resTemplate.exchange(defectsFoundUAT, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
            Bug defectFoundUATObject = defectFoundUATResponse.getBody();

            if(defectFoundUATObject !=null){

                ResponseEntity<Bug> defectsFoundQAResponse = resTemplate.exchange(defectsFoundQA, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
                Bug defectsFoundQAObject = defectsFoundQAResponse.getBody();

                BugView bugViewDefectLeakage = CalculationUtils.calDefectLeakage(defectFoundUATObject,defectsFoundQAObject);
                return bugViewDefectLeakage;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    
    @RequestMapping(value = "/getDefectSeverity", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResultView<BugView> defectSeverity(@RequestParam(name = "fromDate") String fromDate, @RequestParam(name = "toDate") String toDate, @RequestParam(name = "issueType") String issueType) {

    	ResultView resultView = new ResultView<>();
    	
        try {
        	
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
        	Date startDate = sdf.parse(fromDate);        	
        	Date endDate = sdf.parse(toDate);
        	
        	Calendar startCalendar = new GregorianCalendar();
        	startCalendar.setTime(startDate);
        	Calendar endCalendar = new GregorianCalendar();
        	endCalendar.setTime(endDate);
        	
        	int diffYear = endCalendar.get(Calendar.YEAR) - startCalendar.get(Calendar.YEAR);
        	int diffMonth = diffYear * 12 + endCalendar.get(Calendar.MONTH) - startCalendar.get(Calendar.MONTH);
        	
        	List<String> dateList = new ArrayList<>();        	
        	List<String> lables = new ArrayList<>();
        	SimpleDateFormat jiraDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	
        	
        	String date = jiraDateFormat.format(startDate);
        	dateList.add(date);
        	
        	Date initialDate = startDate;
        	for (int i = 0; i < diffMonth+1; i++) {
        		
        		lables.add(jiraDateFormat.format(initialDate));
				
        		Calendar c = Calendar.getInstance();
        		c.setTime(initialDate);
        		c.add(Calendar.MONTH, 1);  // number of days to add
        		
        		initialDate = c.getTime();
        		String datex = jiraDateFormat.format(initialDate);
        		dateList.add(datex);
        		
        		
			}
        	
        	List<BugView> bugVierwList = new ArrayList<>();

        	for (int i = 0; i < dateList.size()-1; i++) {
        		
        		String defectPriority = issueType;
				
        		final String validDefectsSeverity = "https://codegen.atlassian.net/rest/api/2/search?jql=createdDate >= "+dateList.get(i)+" AND createdDate <= "+dateList.get(i+1)+" AND resolution != Invalid AND priority = "+defectPriority+"";
        		
        		final String totalValidDefects = "https://codegen.atlassian.net/rest/api/2/search?jql=createdDate >= "+dateList.get(i)+" AND createdDate <= "+dateList.get(i+1)+" AND resolution != Invalid";

        		ResponseEntity<Bug> validDefectsSeverityResponse = resTemplate.exchange(validDefectsSeverity, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
                Bug validDefectsSeverityObject = validDefectsSeverityResponse.getBody();
                if (validDefectsSeverityObject != null) {

                    ResponseEntity<Bug> totalValidDefectsResponse = resTemplate.exchange(totalValidDefects, HttpMethod.GET, qaMetrixHttpEntity, Bug.class);
                    Bug totalValidDefectsObject = totalValidDefectsResponse.getBody();

                    BugView bugViewinvalidDefects = CalculationUtils.calDefectSeverity(validDefectsSeverityObject, totalValidDefectsObject,defectPriority);

					String label = dateList.get(i);
					bugViewinvalidDefects.setLables(label);

					ResultCount resultCount = new ResultCount();
					resultCount.setDefectWithSeverity(validDefectsSeverityObject.getTotal());
					resultCount.setTotalDefects(totalValidDefectsObject.getTotal());
					bugViewinvalidDefects.setResultCount(resultCount);
                    
                    bugVierwList.add(bugViewinvalidDefects);
                }
        		
			}
        	
        	for (BugView bugView : bugVierwList) {
				System.out.println(bugView.getInvalidBugCountRatio());
			}
        
        	
        	resultView.setStatus(WebConstants.SUCCESS);
        	resultView.setData(bugVierwList);
        	resultView.setLabels(lables);
        	
        	
        } catch (Exception e) {

        	resultView.setStatus(WebConstants.ERROR);
        	
            e.printStackTrace();
        }

        return resultView;

    }
    
    @RequestMapping(value = "/getEfortVariance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
    public ResultView<BugView> effortVariance(@RequestParam(name = "fromDate") String fromDate, @RequestParam(name = "toDate") String toDate, @RequestParam(name = "project") String project) {

    	ResultView resultView = new ResultView<>();
    	
        try {
        	
        	
        	SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
        	Date startDate = sdf.parse(fromDate);  
        	Date endDate = sdf.parse(toDate);
        	
        	SimpleDateFormat jiraDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	String jiraFromDate = jiraDateFormat.format(startDate);
        	String jiraTomDate = jiraDateFormat.format(endDate);
        	
        	
        	
        	List<BugView> bugVierwList = new ArrayList<>();
        	List<String> issueList = new ArrayList<>();

        	 String effortURL = null;
       
				if(project!=null && !project.isEmpty())
				{
					 effortURL = "https://codegen.atlassian.net/rest/api/2/search?jql=\"Project Ref\" = "+project+" AND type = Story AND createdDate >= "+jiraFromDate+" AND createdDate <="+jiraTomDate+"&fields=key,aggregatetimespent,aggregatetimeoriginalestimate";

				}
				else{
					
					 effortURL = "https://codegen.atlassian.net/rest/api/2/search?jql= type = Story AND createdDate >= "+jiraFromDate+" AND createdDate <="+jiraTomDate+"&fields=key,aggregatetimespent,aggregatetimeoriginalestimate";

				}
        		
        		ResponseEntity<Bug> effortVarianceResponse = resTemplate.exchange(effortURL, HttpMethod.GET,qaMetrixHttpEntity, Bug.class);
                Bug effortVarianceObject = effortVarianceResponse.getBody();
                
                if (effortVarianceObject != null) {
                	
                	double totalIssueCount = effortVarianceObject.getTotal();

                	for (Issue issue : effortVarianceObject.getIssues()) {
                		
                		BugView bugVieweffortVariance = CalculationUtils.caleffortVariance(issue);
                		
                        bugVierwList.add(bugVieweffortVariance);
                		issueList.add(issue.getKey());
                		System.out.println(issue.getKey());
						
					}
                	
                	System.out.println(issueList.size());
                    
                }
        	 	
        	for (BugView bugView : bugVierwList) {
				System.out.println(bugView.getInvalidBugCountRatio());
			}
        
        	
        	resultView.setStatus(WebConstants.SUCCESS);
        	resultView.setData(bugVierwList);
        	resultView.setLabels(issueList);
        	
        	
        } catch (Exception e) {

        	resultView.setStatus(WebConstants.ERROR);
        	
            e.printStackTrace();
        }

        return resultView;

    }


}
