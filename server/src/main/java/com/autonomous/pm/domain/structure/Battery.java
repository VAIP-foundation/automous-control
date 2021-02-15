package com.autonomous.pm.domain.structure;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Battery {	// 배터리 상태
	
	private BigDecimal rem;	// 배터리 남은 용량(mA)
	private BigDecimal cap;	// 배터리 최대 용량(mA)
	private BigDecimal volt;	// float.출력전압(V)
	private BigDecimal amp;	// float.출력전류(A)
	
}
