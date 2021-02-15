package com.autonomous.pm.service.bgtask;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.util.CustomizableThreadCreator;

import com.autonomous.pm.service.CallServiceImpl;
import com.autonomous.pm.service.restful.PingPongServiceImpl;
import com.autonomous.pm.service.restful.UsrAthServiceImpl;

@Service
public class BgTaskService {

	public static final Logger logger = LoggerFactory.getLogger(BgTaskService.class);
	public static final int scheduleSize = 1;
	
	@Autowired
	CallServiceImpl callServiceImpl;
	
	@Autowired
	PingPongServiceImpl pingPongService;
	
	@Autowired
	UsrAthServiceImpl usrAthService;
	
	

	public BgTaskService() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Single-tone
	 */
	static BgTaskService self;
//	private void setTask(BgTaskService task) {
//		self = task;
//	}

	private ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(scheduleSize);
	ScheduledFuture<?>[] schedule = new ScheduledFuture<?>[scheduleSize];

	int interval = 1; // 1sec
	long BgTaskMemDBLoader_tick = 0;
	long BgTaskCleanStorage_tick = 0;

	public static BgTaskService instance() {
		return self;
	}

	@PostConstruct
	public void init() {
//		self = this;
//		setTask(this);
		logger.info("BgTaskService.init()");

		updateCallPlan();
//		startBgTaskSendSocketMsgByUI();
	}
	
	/**
	 * @return
	 */
	@Bean
	public TaskScheduler taskScheduler() {
	    TaskScheduler scheduler = new ThreadPoolTaskScheduler();

	    ((ThreadPoolTaskScheduler) scheduler).setPoolSize(2);
	    ((ExecutorConfigurationSupport) scheduler).setThreadNamePrefix("scheduled-task-");
	    ((CustomizableThreadCreator) scheduler).setDaemon(true);

	    return scheduler;
	}
	
	/**
	 */
	@Scheduled(fixedDelay = 10*1000, initialDelay = 1000)
	private void updateCallPlan() {
		callServiceImpl.updateCallPlanP1();
		callServiceImpl.updateCallPlanP2();
	}
	
	
	/**
	 */
	@Scheduled(fixedDelay = 10*1000, initialDelay = 1000)
	private void checkVehicleNetwork() {
		pingPongService.checkSession();
	}
	
	
	/**
	 */
	@Scheduled(fixedDelay = 1000, initialDelay = 1000)
	private void updateWhiteListSession() {
		usrAthService.checkJwtSession();
	}
	
//	@Scheduled(fixedDelay = 1000, initialDelay = 1000)
//	private void startBgTaskSendSocketMsgByUI() {
//		sendEventReport();
//	}
	
}
