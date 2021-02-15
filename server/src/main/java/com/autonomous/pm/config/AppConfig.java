package com.autonomous.pm.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 *
 */
@Component
public class AppConfig {

	public static final Logger logger = LoggerFactory.getLogger(AppConfig.class);

	static AppConfig self = new AppConfig();

	public static AppConfig instance() {
		return self;
	}

	Object lock = new Object();
	Properties properties = null;
	InputStream isProperties;
	long lastModifiedTime = 0;

	private String propFile = "/automous.pm.cnf";

	public AppConfig() {
		load();
	}

	@PostConstruct
	public void init() {
		self = this;
	}

	public AppConfig load() {
		FileInputStream fis = null;
		try {

			Path currentRelativePath = Paths.get("");
			String s = currentRelativePath.toAbsolutePath().toString();
			s += propFile;

			File file = new File(s);
			long lastModifiedTime = file.lastModified();
			if (this.lastModifiedTime == lastModifiedTime) {
				return null;
			}
			
			logger.info("Load config file: {}", s);
			properties = new Properties();
			
			synchronized (lock) {
				fis = new FileInputStream(s);
				properties.load(fis);
			}

			this.lastModifiedTime = lastModifiedTime;
			// properties.load(isProperties);
			
		} catch (FileNotFoundException e) {
			logger.error("[Exception] load() {}", e.toString());
		} catch (IOException e) {
			logger.error("[Exception] load() {}", e.toString());
		} finally {
			try {
				if ( fis != null ) {
					fis.close();
				}
			} catch (IOException e) {
				logger.error("[Exception] load() {}", e.toString());
			}
		}
		return this;
	}

	public String getProperty(String key) {
		synchronized (lock) {
			return properties.getProperty(key);
		}
	}

	public String getProperty(String key, String defaultVal) {
		String val;
		synchronized (lock) {
			val = properties.getProperty(key);
		}
		if (val == null) {
			return defaultVal.trim();
		}
		val = val.trim();
		return val;
	}

	public Integer getPropertyInt(String key) {
		String val;
		synchronized (lock) {
			val = properties.getProperty(key);
		}
		if (val == null)
			return 0;
		val = val.trim();
		return Integer.parseInt(val);
	}

	public Integer getPropertyInt(String key, int defaultVal) {
		String val;
		synchronized (lock) {
			val = properties.getProperty(key);
		}
		if (val == null)
			return defaultVal;
		val = val.trim();
		return Integer.parseInt(val);
	}

	public boolean getPropertyBool(String key, boolean defaultVal) {
		String val;
		synchronized (lock) {
			val = properties.getProperty(key);
		}
		if (val == null)
			return defaultVal;
		val = val.trim();

		switch (val.toLowerCase()) {
		case "true":
			return true;
		case "false":
			return false;
		}
		return false;
	}

	public double getPropertyDouble(String key, double defaultVal) {
		String val;
		synchronized (lock) {
			val = properties.getProperty(key);
		}
		double dVal = 0;
		if (val == null)
			return defaultVal;
		val = val.trim();
		dVal = Double.parseDouble(val);
		return dVal;
	}

	public Boolean getPropertyBool(String key) {
		String val;
		synchronized (lock) {
			val = properties.getProperty(key);
		}
		if (val == null)
			return false;
		val = val.trim();

		switch (val.toLowerCase()) {
		case "true":
			return true;
		case "false":
			return false;
		}
		return false;
	}
}
