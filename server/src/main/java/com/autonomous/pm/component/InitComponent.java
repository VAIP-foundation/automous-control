package com.autonomous.pm.component;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.autonomous.pm.dao.UsrMapper;
//import com.autonomous.pm.repository.MemberRepository;
import com.autonomous.pm.model.Do.TUsr;

@Component
public class InitComponent implements ApplicationRunner {

	public static final Logger logger = LoggerFactory.getLogger(InitComponent.class);

	@Autowired
	private UsrMapper usersMapper;


	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public void run(ApplicationArguments args) {

		logger.info("app run ");

		List<TUsr> u = usersMapper.select();
		if(u.size() == 0){
			TUsr user = new TUsr();
			user.setLoginId("admin");
			user.setUsrNm("admin");
			user.setPwd(passwordEncoder.encode("1234"));
			user.setIdUgrp(1L);
			usersMapper.insert(user);
		}
	}
}