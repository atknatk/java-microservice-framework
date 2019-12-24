package com.esys.main.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;


@Configuration
@EnableScheduling
@Slf4j
public class SchedulingConfig {

	
	
	@Scheduled(cron = "0 0/30 23 * * ?")
	public void scheduleTaskUsingCronExpression() {
	  
	}
}