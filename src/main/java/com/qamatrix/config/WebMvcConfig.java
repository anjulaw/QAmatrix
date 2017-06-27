package com.qamatrix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qamatrix.utils.WebConstants;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import java.util.List;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.JstlView;
import java.util.List;

@Configuration
@EnableWebMvc
@PropertySource({ "file:${web.server.config}/application.properties"})
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Autowired
	Environment env;

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}
	
	

	@Bean(name = "qaMetrixHttpEntity")
	public HttpEntity qaMetrixHttpEntity() {

		String plainCreds = env.getProperty(WebConstants.CREDENTIALS);
		byte[] plainCredsBytes = plainCreds.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		HttpEntity<String> request = new HttpEntity<String>(headers);

		return request;
	}
	
	@Autowired
	@Qualifier("jstlViewResolver")
	private ViewResolver jstlViewResolver;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		String staticAngularAppPath = env.getProperty(WebConstants.STATIC_WEB_ANGULAR_APP_PATH);
		String staticConfigFilePath = env.getProperty(WebConstants.STATIC_WEB_ANGULAR_APP_CONFIG_PATH);
		
		String staticTemplateCssPath = env.getProperty(WebConstants.STATIC_WEB_TEMPLATE_CSS_PATH);
		String staticTemplateImagePath = env.getProperty(WebConstants.STATIC_WEB_TEMPLATE_IMAGE_PATH);
	
		
		
		if (staticAngularAppPath != null) {
			registry.addResourceHandler("/**")
			.addResourceLocations(WebConstants.FILE_PROTOCAL + staticAngularAppPath)
			.addResourceLocations(WebConstants.FILE_PROTOCAL + staticConfigFilePath)
			.addResourceLocations(WebConstants.FILE_PROTOCAL + staticTemplateCssPath)
			.addResourceLocations(WebConstants.FILE_PROTOCAL + staticTemplateImagePath);
		}
		
	}

	@Bean
	@DependsOn({ "jstlViewResolver" })
	public ViewResolver viewResolver() {
		return jstlViewResolver;
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("forward:/index.html");
	}

	@Bean(name = "jstlViewResolver")
	public ViewResolver jstlViewResolver() {
		UrlBasedViewResolver resolver = new UrlBasedViewResolver();
		resolver.setPrefix(""); // NOTE: no preffix here
		resolver.setViewClass(JstlView.class);
		//resolver.setSuffix(".html"); // NOTE: no suffix here
		return resolver;
	}
	

	@Bean(name = "resTemplate")
	public RestTemplate restTemplate() {

		try {

			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy).build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();

			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

			requestFactory.setHttpClient(httpClient);

			RestTemplate restTemplate = new RestTemplate(requestFactory);

			return restTemplate;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return null;

	}

}