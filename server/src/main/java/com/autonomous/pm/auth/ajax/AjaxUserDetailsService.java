package com.autonomous.pm.auth.ajax;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.autonomous.pm.auth.FailCountOverException;
import com.autonomous.pm.auth.UsrDetailsImpl;
import com.autonomous.pm.dao.UsrGrpMapper;
import com.autonomous.pm.dao.UsrMapper;
import com.autonomous.pm.model.Usr;
import com.autonomous.pm.model.Do.TUsr;
import com.autonomous.pm.model.Do.TUsrGrp;
import com.autonomous.pm.service.LoginService;

@Component
public class AjaxUserDetailsService implements UserDetailsService {

	@Autowired
	UsrMapper usrMapper;
	
	@Autowired
	UsrGrpMapper userGrpsMapper;
	
	@Autowired
	LoginService loginService;

	@Override
	public UserDetails loadUserByUsername(String username) {
		
		// 로그인가능여부를 확인한다.
		if (loginService.isNotPossible(username)) {
			throw new FailCountOverException("Fail Count Over.::loginId=" + username);
		}
		
		Usr user = usrMapper.findByLoginId(username);	// == loginId

		if (user == null) {
//			throw new UsernameNotFoundException(username + "라는 사용자가 없습니다.");
//			throw new UsernameNotFoundException("Not found User.::loginId=" + username);
			throw new BadCredentialsException("Bad credentials.::loginId=" + username);
		}
//		TUsrGrp usrGrp = userGrpsMapper.findById(user.getIdUgrp());
//		TUsrGrp usrGrp = user.getUsrGrp();
//		String athCd = Integer.toString(usrGrp.getAthCd());
		String athCd = Integer.toString(user.getAthCd());
		return new UsrDetailsImpl(user, AuthorityUtils.createAuthorityList(athCd));
//		return new UserDetailsImpl(user, AuthorityUtils.createAuthorityList(user.getcAuthCode()));
    }
}
