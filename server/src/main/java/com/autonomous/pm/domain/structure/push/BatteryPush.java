package com.autonomous.pm.domain.structure.push;

import org.springframework.beans.BeanUtils;

import com.autonomous.pm.domain.structure.Battery;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BatteryPush extends Battery {	// UI PUSH를 위한 구조체. 일부 항목을 추가하였다.
	
	private boolean recharge;
	
	public BatteryPush() {
	}
	
	public BatteryPush(Battery battery) {
		if ( battery != null ) {
			BeanUtils.copyProperties(battery, this);
		}
	}
}