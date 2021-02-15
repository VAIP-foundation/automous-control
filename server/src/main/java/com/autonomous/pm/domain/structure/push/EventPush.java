package com.autonomous.pm.domain.structure.push;

import org.springframework.beans.BeanUtils;

import com.autonomous.pm.domain.structure.Event;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EventPush extends Event {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
	
	private Long idV;	// 차량ID
	private String vrn;	// 차량번호
	
	public EventPush() {
	}
	
	public EventPush(Long idV, String vrn, Event event) {
		this.setIdV(idV);
		this.setVrn(vrn);
		BeanUtils.copyProperties(event, this);
	}
}