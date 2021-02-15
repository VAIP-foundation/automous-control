package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.model.Dto.IisAfsQueue;

public interface CallChangeService {
	
	void checkCallChange(List<IisAfsQueue> queueList);
	
}
