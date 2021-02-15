package com.autonomous.pm.core.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.autonomous.pm.config.AppProperties;
import com.autonomous.pm.domain.structure.mem.CommonMem;

/*
 * DrivingInfo	// index 0 
 * TripInfo		// index 1
 * SensorInfo	// index 2
 * Event		// index 3
 * RouteInfo	// index 4
 */

public enum GlobalQueue {
	INSTANCE;

	public BlockingQueue<CommonMem>[] pmMeseageQ = null;//new LinkedBlockingQueue[9];

	@SuppressWarnings("unchecked")
	GlobalQueue() {
		int cnt = AppProperties.instance().getPropertyInt("QINDEX.Total");
		pmMeseageQ = new LinkedBlockingQueue[cnt];
		CreateQueue(cnt);
	}

	public BlockingQueue<CommonMem> getPmMeseageQ(int index) {
		return pmMeseageQ[index];
	}

	public void CreateQueue(int cnt) {
		
		for (int i = 0; i < cnt ; i++) {
			pmMeseageQ[i] = new LinkedBlockingQueue<CommonMem>();
		}
	}

	public void RemoveQueue() {

	}
	
}