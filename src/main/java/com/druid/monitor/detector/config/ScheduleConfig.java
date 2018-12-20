package com.druid.monitor.detector.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@ComponentScan("com.druid")
@EnableScheduling
public class ScheduleConfig {

	@Bean
	public TaskScheduler defaultPoolScheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setThreadNamePrefix("defaultPoolScheduler");
		scheduler.setPoolSize(20);
		return scheduler;
	}
}