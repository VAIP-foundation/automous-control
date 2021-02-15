package com.autonomous.pm.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@Configuration
@EnableScheduling
public class CommonConfig {

	Map<String, PasswordEncoder> encoders = new HashMap<>();

	@Bean
	public PasswordEncoder passwordEncoder() {

		String idForEncode = "sha256";
		// String idForEncode = "bcrypt";

		encoders.put("bcrypt", new BCryptPasswordEncoder());
	//	encoders.put("noop", NoOpPasswordEncoder.getInstance());
		encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
		encoders.put("sha256", new StandardPasswordEncoder());

		return new DelegatingPasswordEncoder(idForEncode, encoders);
	}
}
