package com.weswu.clouduuid;

import com.weswu.clouduuid.utils.LoadProps;
import com.weswu.clouduuid.utils.StackDriverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Properties;


@SpringBootApplication
@ConfigurationProperties(prefix = "")
@PropertySource({"classpath:application.properties"})
@ComponentScan(basePackages = "com.weswu.clouduuid")
public class ClouduuidApplication {

	public static void main(String[] args) {
		setLogbackVariables();
		setupStackDriver();
		Logger logger = LoggerFactory.getLogger(ClouduuidApplication.class);
		SpringApplication.run(ClouduuidApplication.class, args);
	}

	public static void setLogbackVariables() {
		//set the custome credential for google-cloud-logging-logback
		try {
			Properties props = LoadProps.fromAppPros();
			URL res = ClouduuidApplication.class.getClassLoader().getResource("application.properties");
			File file = Paths.get(res.toURI()).toFile();
			String absolutePath = file.getParent();
			System.setProperty("gcp.credential.in.logback", absolutePath + "/" + props.getProperty("gcp.service.account.file"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void setupStackDriver(){
		try {
			StackDriverConfig.setupOpenCensusAndStackDriver();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}

