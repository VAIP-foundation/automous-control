package com.autonomous.pm.service;

import java.util.List;

import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.model.CallPlanP1;
import com.autonomous.pm.model.CallPlanP2;
import com.autonomous.pm.model.Dto.CmdHst;
import com.autonomous.pm.model.Dto.IisAfsQueue;
import com.autonomous.pm.model.Dto.PmAssgn;

public interface CmdHstService {
	
	void insertCmdHst(Long idV, String cmd, String mthd, String rqmsg, String sendStr, String mId, String ackStr);
	void insertCmdHst(CmdHst cmdHst);
	void updateCmdHstByMid(String mId, String ackStr);
	void insertCmdHstByGetCall(String vrn, Call call);
	
}
