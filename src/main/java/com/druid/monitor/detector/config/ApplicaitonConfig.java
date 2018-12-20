package com.druid.monitor.detector.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class ApplicaitonConfig {
	
	/**
	 * Required for Druid Monitor SQL stat.
	 */
	@Bean
	@ConfigurationProperties(prefix="spring.datasource")
	public DataSource druidDataSource() {
	   return new DruidDataSource();
	}
}

