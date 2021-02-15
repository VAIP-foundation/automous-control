package com.autonomous.pm.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.CmdHstMapper;
import com.autonomous.pm.domain.structure.Call;
import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.Do.VVhcl;
import com.autonomous.pm.model.Dto.CmdHst;
import com.autonomous.pm.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CmdHstServiceImpl implements CmdHstService {
	
	@Autowired CmdHstMapper cmdHstMapper;
	
	/**
	 * @param vrn
	 * @param cmd
	 * @param mthd
	 * @param rqmsg
	 * @param sendStr
	 * @param mId
	 * @param ackStr
	 */
	public void insertCmdHst(String vrn, String cmd, String mthd, String rqmsg, String sendStr, String mId, String ackStr) {
		VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
		if (vhcl != null ) {
			Long idV = vhcl.getIdV();
			insertCmdHst(idV, cmd, mthd, rqmsg, sendStr, mId, ackStr);
		}
	}
	@Override
	public void insertCmdHst(Long idV, String cmd, String mthd, String rqmsg, String sendStr, String mId, String ackStr) {
		String dts = DateUtil.getUtcDatetimeByFormat(new Date(), "YYYYMMDDhhmiss");
		
		CmdHst cmdHst = CmdHst.builder()
			    .idV(idV)		
			    .dts(dts)		
			    .cmd(cmd)		
			    .mthd(mthd)		
			    .rqMsg(rqmsg)	
			    .sendStr(sendStr)		
			    .mId(mId)			
			    .ackStr(ackStr)		
			    .cDt(null)		
			    .mDt(null)		
				.build();
		
		insertCmdHst(cmdHst);
	}

	@Override
	public void insertCmdHst(CmdHst cmdHst) {
		cmdHstMapper.insertSelective(cmdHst);
	}

	@Override
	public void insertCmdHstByGetCall(String vrn, Call call) {
		VVhcl vhcl = MemDB.VHCL.selectAll().stream().filter(v->v.getVrn().equals(vrn)).findFirst().orElse(null);
		Long idV = vhcl != null ? vhcl.getIdV() : 0;
		String cmd = "";
		String mthd = "G";
		String rqmsg = "/api/v1/call/" + vrn;
		String sendStr = call.toString();
		String mId = call.getMid();
		String ackStr = null;
		if ("p1".equals(call.getPrio()) && "wait".equals(call.getSub()) ) {
			cmd = "CALL-RETURN";
			rqmsg = "/api/v1/" + call.getTo().getTerm() + "/call/p1/poi?idV=" + idV;
		} else if ( "batlow".equals(call.getSub()) ) {
			cmd = "CALL-BATLOW";
		} else {
			cmd = "CALL";
		}
		insertCmdHst(vrn, cmd, mthd, rqmsg, sendStr, mId, ackStr);
	}

	/**
	 */
	@Override
	public void updateCmdHstByMid(String mId, String ackStr) {
		CmdHst cmdHst = CmdHst.builder()
			    .mId(mId)
			    .ackStr(ackStr)
				.build();
		cmdHstMapper.updateByMidSelective(cmdHst);
	}
}
