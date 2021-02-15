package com.autonomous.pm.service.restful;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autonomous.pm.dao.UsrGrpMapper;
import com.autonomous.pm.dao.UsrMapper;
import com.autonomous.pm.dao.gen.TUsrGrpMapper;
import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Do.TUsr;
import com.autonomous.pm.model.Dto.ReqAdminUsr;
import com.autonomous.pm.model.Dto.ReqAdminUsrAth;
import com.autonomous.pm.model.Dto.ReqAdminUsrGrp;
import com.autonomous.pm.model.Dto.ResAdminUsrGrp;

@Service
public class UsrGrpServiceImpl implements UsrGrpService {

	public static final Logger logger = LoggerFactory.getLogger(UsrGrpServiceImpl.class);

	@Autowired
	UsrGrpMapper usrGrpMapper;
	
	@Autowired
	UsrMapper usrMapper;

	@Override
	public List<ResAdminUsrGrp> getAdminUsrGrpLs(String grpNm) {
		return usrGrpMapper.getAdminUsrGrpLs(grpNm);
	}

	@Override
	public int postAdminUsrGrpLs(List<ReqAdminUsrGrp> reqParam) {
		
		// 해당 사용자권한 코드를 사용자 그룹에서 사용 중인지 체크하여
		// 사용 중일 경우 삭제하지 못하도록 한다.
		for (ReqAdminUsrGrp adminUsrGrp : reqParam) {
			if ( adminUsrGrp.getdFlg() != null && adminUsrGrp.getdFlg() == 1 ) {
				Long idUgrp = adminUsrGrp.getIdUgrp();
				List<TUsr> usrList = usrMapper.getUsrLsByIdUgrp(idUgrp);
				if ( usrList.size() > 0 ) {
					return -1;
				}
			}
		}
		
		
		for (ReqAdminUsrGrp adminUsrGrp : reqParam) {
			usrGrpMapper.postAdminUsrGrpLs(adminUsrGrp.toEntity());
		}
		return 0;
	}

	
	

}
