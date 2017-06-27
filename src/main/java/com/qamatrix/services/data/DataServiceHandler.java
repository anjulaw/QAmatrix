package com.qamatrix.services.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.qamatrix.domain.DataCache;
import com.qamatrix.domain.JiraUser;
import com.qamatrix.domain.Project;

@Service("dataService")
public class DataServiceHandler implements IDataService {

	@Autowired
	RestTemplate resTemplate;

	@Autowired
	HttpEntity qaMetrixHttpEntity;

	@Override
	public void loadData() {

		List<Project> response = resTemplate.exchange("https://codegen.atlassian.net/rest/api/2/project",
				HttpMethod.GET, qaMetrixHttpEntity, new ParameterizedTypeReference<List<Project>>() {
				}).getBody();

		
		DataCache.projectData.put("PROJECTS", response);
		
		List<JiraUser> responseUsers = resTemplate.exchange("https://codegen.atlassian.net/rest/api/2/user/search?username=%&maxResults=1000&includeActive=true",
				HttpMethod.GET, qaMetrixHttpEntity, new ParameterizedTypeReference<List<JiraUser>>() {
				}).getBody();
		
		
		DataCache.usertData.put("USERS", responseUsers);
	}

	@Override
	public List<JiraUser> getJiraUsers() {
		
		return DataCache.usertData.get("USERS");
	}

	@Override
	public List<Project> getProjects() {
		
		return DataCache.projectData.get("PROJECTS");
	}

}
