package com.druid.monitor.detector.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.druid.monitor.detector.component.BadCodeDetectorComponent;

@Component
public class BadCodeDetectorSchedule {

	@Autowired
	private BadCodeDetectorComponent monitorDetectorComponent;
	
	/**
	 * Schedule run start on 3:01, and run every 3 minutes.
	 */
	@Scheduled(cron = "1 3/3 * * * ?")
//	@RedisLock(value = "lock:schedule:monitorAndDetectBadMethodExcution", keepMills = 180000, action = LockFailAction.GIVEUP)
	public void monitorAndDetectBadMethodExcution() {
		monitorDetectorComponent.monitorAndDetectBadMethodExcution();
	}
	

	/**
	 * Schedule run start on 6:10, and run every 6 minutes.
	 */
	@Scheduled(cron = "10 6/6 * * * ?")
	//@RedisLock(value = "lock:schedule:monitorAndDetectBadSqlExcution", keepMills = 360000, action = LockFailAction.GIVEUP)
	public void monitorAndDetectBadSqlExcution() {
		monitorDetectorComponent.monitorAndDetectBadSqlExcution();
	}
}
