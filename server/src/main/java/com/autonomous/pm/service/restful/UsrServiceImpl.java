package com.autonomous.pm.service.restful;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.autonomous.pm.dao.UsrMapper;
import com.autonomous.pm.dao.gen.TUsrMapper;
import com.autonomous.pm.domain.Exception.BaseException;
import com.autonomous.pm.domain.common.SearchParam;
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Do.TUsr;
import com.autonomous.pm.model.Dto.ReqAdminUsr;
import com.autonomous.pm.model.Dto.ResAdminPoi;
import com.autonomous.pm.model.Dto.ResAdminUsr;
import com.autonomous.pm.util.StringUtil;
import com.autonomous.pm.util.ValidationUtil;
import com.autonomous.pm.web.controller.v1.AdminUsrRestController;

@Service
public class UsrServiceImpl implements UsrService {

	public static final Logger logger = LoggerFactory.getLogger(UsrServiceImpl.class);

	@Autowired
	UsrMapper usrMapper;
	
	@Autowired
	TUsrMapper tUsrMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private PlatformTransactionManager transactionManager;

	public List<Usr> searchUsr(SearchParam p) {
		return usrMapper.searchUsr(p);
	}

	/**
	 * 
	 * @param usrNm
	 * @return
	 */
	public Usr findUsrById(String idUsr) {
		SearchParam p = new SearchParam();
		p.setIdUsr(idUsr);
		List<Usr> r = usrMapper.searchUsr(p);
		if (r.size() == 0) {
			return null;
		}
		return r.get(0);
	}
	
	/**
	 * 
	 * @param usrNm
	 * @return
	 */
	public Usr findUsrByLoginId(String loginId) {
		SearchParam p = new SearchParam();
		p.setLoginId(loginId);
		List<Usr> r = usrMapper.searchUsr(p);
		if (r.size() == 0) {
			return null;
		}
		return r.get(0);
	}

	/**
	 * 
	 */
	public void deleteUsrs(ArrayList<Long> idUsrList) {
		DefaultTransactionDefinition def = null;
		TransactionStatus status = null;
		
		def = new DefaultTransactionDefinition();
		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		status = transactionManager.getTransaction(def);
		try {
			for (Long idUsr : idUsrList) {
				TUsr usr = usrMapper.findById(idUsr);
				usr.setdFlg((byte) 0);
				usrMapper.upsert(usr);
			}
			transactionManager.commit(status);
		} catch (RuntimeException e) {
			transactionManager.rollback(status);
			logger.error("deleteSites ERROR " + e.getMessage());
			throw new BaseException(e.getMessage());
		}
	}

	/**
	 * 
	 * @param site
	 */
	public void upsertUsr(TUsr usr) {
		usrMapper.upsert(usr);
	}

	@Override
	public List<ResAdminUsr> getAdminUsrLs(Integer idUgrp, String usrNm) {
		return usrMapper.getAdminUsrLs(idUgrp, usrNm);
	}

	@Override
	public void postAdminUsrLs(List<ReqAdminUsr> reqParam, BindingResult bindingResult) throws BindException {

		for (ReqAdminUsr adminUsr : reqParam) {
			String pwd = adminUsr.getPwd();
			
			if ( adminUsr.getIdUsr() == null && StringUtil.isEmpty(pwd) ) {
				String errTxt = "등록시 로그인암호는 필수 입력 값입니다.";
				FieldError error = new FieldError("reqAdminUsrList", "pwd", errTxt);
				bindingResult.addError(error);
				throw new BindException(bindingResult);
			} else if (adminUsr.getIdUsr() != null && StringUtil.isEmpty(pwd)){
				TUsr current = usrMapper.findById(adminUsr.getIdUsr());
				adminUsr.setPwd(current.getPwd());
			}
			
			
			if ( pwd != null && !pwd.contains("{sha256}") && !pwd.contains("{bcrypt}") && !pwd.contains("{pbkdf2}") && !pwd.contains("{noop}") ) {
				
				if ( ValidationUtil.isNotPasswordValidate(pwd) ){
					String errTxt = ValidationUtil.passwordValidator(pwd);
					FieldError error = new FieldError("reqAdminUsrList", "pwd", errTxt);
					bindingResult.addError(error);
					throw new BindException(bindingResult);
				} else {
					String encPassword = passwordEncoder.encode(pwd);
					adminUsr.setPwd(encPassword);
				}
			}

			usrMapper.upsert(adminUsr.toEntity());	
		}
	}

}
