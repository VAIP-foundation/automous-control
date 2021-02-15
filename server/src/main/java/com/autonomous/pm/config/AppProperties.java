package com.autonomous.pm.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class AppProperties{

//	public static final Logger logger = LoggerFactory.getLogger(AppProperties.class);

	static AppProperties self = new AppProperties();

	public static AppProperties instance() {
		return self;
	}

	Properties properties = new Properties();
	//FileInputStream 
	InputStream isProperties;
	// 프로퍼티 파일 위치
//	private String propFile = "src/main/resources/application.properties";
	private String propFile = "application.properties";

	public AppProperties() {
		try {
			isProperties = this.getClass().getClassLoader().getResourceAsStream(propFile);
//			properties.load(isProperties);
			
//			isProperties = new FileInputStream(propFile);

			properties.load(new java.io.BufferedInputStream(isProperties));
			
		} catch (IOException e) {
//			logger.error("[Exception] AppProperties(): {}", e.getMessage());
			System.out.println("AppProperties Exception occurred: ");
		}
	}
	
	@PostConstruct
	public void init() {
		self = this;
	}
	
	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public Integer getPropertyInt(String key) {
		String val = properties.getProperty(key);
		if (val == null)
			return 0;
		val = val.trim();
		return Integer.parseInt(val);
	}
	

}
