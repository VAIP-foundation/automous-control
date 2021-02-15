package com.autonomous.pm.domain.structure;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Charging { // 충전상태.
	
	private Integer chg_tm;	// 충전 시간(sec)-(분단위갱신)
	private Integer rem_tm;	// 완충까지 남은 시간(sec)-(분단위갱신)
	
}
