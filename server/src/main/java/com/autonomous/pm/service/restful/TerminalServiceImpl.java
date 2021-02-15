package com.autonomous.pm.service.restful;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Service
public class TerminalServiceImpl implements TerminalService {

	public static final Logger logger = LoggerFactory.getLogger(TerminalServiceImpl.class);

	private enum Terminal { T1, T2 }
	
	@Override
	public List<String> getAllTerminal() {
		List<String> terminals = Arrays.asList(Terminal.T1.toString(), Terminal.T2.toString());
		return terminals;
	}
	
	@Override
	public boolean isT1Terminal(String ty) {
		// T1=입국장=ARV
		return "ARV".equals(ty);
	}
	
	@Override
	public boolean isT2Terminal(String ty) {
		// T2=출국장=DPT
		return "DPT".equals(ty);
	}
	
	@Override
	public String getTerminalByType(String ty) {
		if ( isT1Terminal(ty) ) {
			return Terminal.T1.toString();
		} else if ( isT1Terminal(ty) ) {
			return Terminal.T2.toString();
		} else {
			return null;
		}
	}
	
	@Override
	public String checkTerminal(BindingResult bindingResult, String terminal) {
		
		// 전체 터미널목록 조회
		List<String> terminals = getAllTerminal();
		
		// 전체 터미널 리스트중 넘어온 terminal 값으로 filter
		terminals = terminals.stream().filter(v -> v.equals(terminal)).collect(Collectors.toList());
		if (terminals.size() == 0) {	// 없으면 에러체크
			terminals = getAllTerminal();
			String validParams = String.join(", ", terminals);
			FieldError e = new FieldError("PathVariable", "terminal", "terminal 는 [" + validParams + "] 중 하나의 값이어야 합니다.");
			bindingResult.addError(e);
		}
		return terminal;
	}
	
	public FieldError checkTerminal(String terminal) {
		
		// 전체 터미널목록 조회
		List<String> terminals = getAllTerminal();
		FieldError e = null;
		
		// 전체 터미널 리스트중 넘어온 terminal 값으로 filter
		terminals = terminals.stream().filter(v -> v.equals(terminal)).collect(Collectors.toList());
		if (terminals.size() == 0) {	// 없으면 에러체크
			terminals = getAllTerminal();
			String validParams = String.join(", ", terminals);
			e = new FieldError("PathVariable", "terminal", "terminal 는 [" + validParams + "] 중 하나의 값이어야 합니다.");
		}
		return e;
	}
	

}
