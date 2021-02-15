package com.autonomous.pm.dao;

import java.util.List;

import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Do.TUsr;
import com.autonomous.pm.model.Dto.ReqAdminUsr;
import com.autonomous.pm.model.Dto.ResAdminUsr;

public interface UsrMapper {
	
    List<TUsr> select();
    TUsr findById(Long idUsr);
    Usr findByLoginId(String loginId);
    
    int insert(TUsr u);
    int update(TUsr u);
    int upsert(TUsr u);
    List<Usr> searchUsr(SearchParam search);
    
    List<ResAdminUsr> getAdminUsrLs(Integer idUgrp, String usrNm);
//    int postAdminUsrLs(ReqAdminUsr reqParam);
    List<TUsr> getUsrLsByIdUgrp(Long idUgrp);

}