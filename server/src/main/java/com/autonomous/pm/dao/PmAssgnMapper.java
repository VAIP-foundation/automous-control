package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.model.Do.TTrpRaw;
import com.autonomous.pm.model.Dto.PmAssgn;
import com.autonomous.pm.model.Dto.ReqFromToDay;
import com.autonomous.pm.model.Dto.RestRawCall;

public interface PmAssgnMapper {
	List<PmAssgn> selectLastAll();
	
	PmAssgn selectLastCallByIdV(Long idV);
	PmAssgn selectCallByTrip(TTrpRaw trpRaw);
	PmAssgn selectWaitCallByTrip(Long idV, Long toPoi);
	PmAssgn selectOngoingCallByTrip(Long idV, Long toPoi);
	
	List<PmAssgn> selectInitRows();
    int deleteByPrimaryKey(Long idCall);
    int insert(PmAssgn record);
    int insertSelective(PmAssgn record);
    PmAssgn selectByPrimaryKey(Long idCall);
    int updateByPrimaryKeySelective(PmAssgn record);
    int updateByPrimaryKey(PmAssgn record);
    
    Integer selectSubseqByToday(String today);
    List<PmAssgn> selectStandByP1Call(Long idGop);
    
    int rollbackAtStart(TTrpRaw trpRaw);
    
    
    List<RestRawCall> selectRawList(ReqFromToDay reqParam);
}