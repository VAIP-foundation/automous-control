package com.autonomous.pm.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.autonomous.pm.memcache.MemDB;
import com.autonomous.pm.model.LoginInfo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {

	
	@Value("${login.limit.count:5}")
	private Integer loginLimitCount;
	@Value("${login.limit.min:10}")
	private Integer loginLimitMin;
	
	@Override
	public void success(String loginId) {
		if ( loginId == null ) {
			log.error("loginId is null");
			throw new NullPointerException();
		}
		
		LoginInfo li = new LoginInfo();
		li.setLoginId(loginId);
		li.setLastLoginDt(new Date());
		li.setLastFailDt(null);
		li.setFailCount(0);

		MemDB.LOGIN_INFO.insertSafety(loginId, li);
		
		log.info("LoginServiceImpl.success: " + loginId);
		log.info("LoginServiceImpl.success: " + MemDB.LOGIN_INFO.selectAll());
	}
	

	@Override
	public LoginInfo fail(String loginId) {
		if ( loginId == null ) {
			log.error("loginId is null");
			throw new NullPointerException();
		}
		
		LoginInfo li = MemDB.LOGIN_INFO.select(loginId);
		if ( li == null ) {
			li = new LoginInfo();
		}
		li.setLoginId(loginId);
//		li.setLastLoginDt();
		li.setLastFailDt(new Date());
		li.setFailCount(li.getFailCount()+1);
		
		MemDB.LOGIN_INFO.insertSafety(loginId, li);

		log.info("LoginServiceImpl.fail: " + loginId);
		log.info("LoginServiceImpl.fail: " + MemDB.LOGIN_INFO.selectAll());
		
		return li;
	}
	
	@Override
	public Date getPossibleDt(String loginId) {
		Date possibleDt = new Date();
		if ( loginId == null ) {
			possibleDt = new Date(possibleDt.getTime() + loginLimitMin*60*1000);
		} else {
			LoginInfo li = MemDB.LOGIN_INFO.select(loginId);
			if ( li != null && li.getLastFailDt() != null ) {
				possibleDt = new Date(li.getLastFailDt().getTime() + loginLimitMin*60*1000); 
			} else {
				possibleDt = new Date(possibleDt.getTime() + loginLimitMin*60*1000);
			}
		}
		return possibleDt;
	}
	
	@Override
	public String getPossibleDtString(String loginId) {
		Date possibleDt = getPossibleDt(loginId);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String possibleDtString  = sdf.format(possibleDt);
		return possibleDtString;
	}
	
	
	@Override
	public boolean isPossible(String loginId) {
		
		boolean isPossible = false;
		LoginInfo li = MemDB.LOGIN_INFO.select(loginId);
		// 접속기록이 없으면 로그인시도 가능
		if ( li == null ) {
			isPossible = true;
		
		// 접속실패 횟수가 5 이하일 경우 로그인시도 가능
		} else  if (li.getFailCount() < loginLimitCount) {
			isPossible = true;
		
		// 접속실패횟수가 5를 넘었을 경우 시간에 따라 판단.
		} else if ( li.getLastFailDt() != null ) {
			Long lastFailTime = li.getLastFailDt().getTime();
			Date currentDate = new Date();
			Long currentTime = currentDate.getTime();
			
			
			// 마지막 접속시간이 현재시간보다 10분이 초과했을 경우 로그인시도 가능
			// 실패횟수를 5->0으로 초기화 해준다.
			if ( (lastFailTime + loginLimitMin*60*1000) > currentTime ) {
				isPossible = false;
			} else {
				li.setFailCount(0);
				MemDB.LOGIN_INFO.insert(loginId, li);
				isPossible = true;
			}
			
		} else {
			isPossible = true;
		}
		
		log.info("LoginServiceImpl.isPossible: " + loginId);
		log.info("LoginServiceImpl.isPossible: " + isPossible);
		log.info("LoginServiceImpl.isPossible: " + MemDB.LOGIN_INFO.selectAll());
		
		return isPossible;
	}
	
	@Override
	public boolean isNotPossible(String loginId) {
		return !isPossible(loginId);
	}


	
}
