package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.model.CallPlanP1;
import com.autonomous.pm.model.CallPlanP2;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.PmAssgn;

public interface CallService {
	
	List<CallPlanP1> createP1CallList();
	List<CallPlanP2> createP2CallList();
	
	Call getCall(Long idV);
	Call getCallP1(String term, Long idV);
	Call getCallP2ByBlock(String term, Long idV);
	Call getCallP2ByLastKnown(String term, Long idV);
	Call getCallP2(String term, Long idV);
	
//	void addCallP1(PmAssgn pmAssgn);
	void addCallP1(String term, Long idPoi, Long idV, Integer repeat) throws Exception;
	void addCallP1(String term, Long toPoi, Long idV, Integer repeat, Integer repeatAct, String callTy) throws Exception;
	
	void updateCallPlanP1();
	void updateCallPlanP2();
	
	void updateDirtyP2CallList(List<IisAfsQueue> queueList);

	
}
