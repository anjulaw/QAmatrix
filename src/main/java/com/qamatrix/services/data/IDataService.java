package com.qamatrix.services.data;

import java.util.List;

import com.qamatrix.domain.JiraUser;
import com.qamatrix.domain.Project;

public interface IDataService {

	public void loadData();

	public List<JiraUser> getJiraUsers();
	
	public List<Project> getProjects();

}
