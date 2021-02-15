package com.autonomous.pm.service.restful;

import java.util.List;

import org.springframework.validation.BindingResult;

public interface TerminalService {
	
	List<String> getAllTerminal();
	boolean isT1Terminal(String ty);
	boolean isT2Terminal(String ty);
	String getTerminalByType(String ty);
	
	String checkTerminal(BindingResult bindingResult, String terminal);
}
