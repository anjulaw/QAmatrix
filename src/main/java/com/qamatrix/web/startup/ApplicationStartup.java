package com.qamatrix.web.startup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.qamatrix.services.data.IDataService;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	IDataService dataService;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		
		dataService.loadData();

		return;
	}

} // c