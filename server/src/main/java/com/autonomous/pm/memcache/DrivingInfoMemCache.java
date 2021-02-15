package com.autonomous.pm.memcache;

import java.math.BigDecimal;

import com.autonomous.pm.model.Do.TDrvRawYymm;

public class DrivingInfoMemCache extends TDrvRawYymm {
	/**
	 */
	private BigDecimal sumOfVss = new BigDecimal(0); // TRIP 내에서의 속도의 전체 SUM 값
	private BigDecimal countOfVss = new BigDecimal(0); // 평균 속도를 내기 위한 분모 값. 속도 add 한 횟수

	/**
	 * @param newVss
	 */
	public void addSumOfVss(BigDecimal newVss) {
		this.sumOfVss = this.sumOfVss.add(newVss);
		this.countOfVss = this.countOfVss.add(new BigDecimal(1));
	}

	/**
	 * @return
	 */
	public BigDecimal calcAvgOfVss() {
		BigDecimal avg = this.sumOfVss.divide(this.countOfVss, 2, BigDecimal.ROUND_HALF_UP);
		return avg;
	}

	/**
	 * @return
	 */
	public BigDecimal getNumOfVss() {
		return countOfVss;
	}

	/**
	 */
	private BigDecimal sumOfCoolantTemp = new BigDecimal(0f); // TRIP 내에서의 냉각수 온도 의 전체 SUM 값
	
	/**
	 */
	private BigDecimal countOfCoolantTemp = new BigDecimal(0f); // 평균 냉각수 온도 를 내기 위한 분모 값. 냉각수 온도 add 한 횟수

	/**
	 * 
	 * 
	 * @param newVss
	 */
	public void addSumOfCoolantTemp(BigDecimal newVss) {
		this.sumOfCoolantTemp = this.sumOfCoolantTemp.add(newVss);
		this.countOfCoolantTemp = this.countOfCoolantTemp.add(new BigDecimal(1));
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public BigDecimal calcAvgOfCoolantTemp() {
		BigDecimal avg = this.sumOfCoolantTemp.divide(this.countOfCoolantTemp, 2, BigDecimal.ROUND_HALF_UP);
		return avg;
	}
	
	/**
	 */
	private BigDecimal tripDistanceM = new BigDecimal(0.0d);
	/**
	 * @param meter
	 * @return
	 */
	public BigDecimal addTripDistanceM(BigDecimal meter) {
		tripDistanceM = tripDistanceM.add(meter);
		return tripDistanceM;
	}
	public BigDecimal getTripDistanceM() {
		return tripDistanceM;
	}

	/**
	 */
	private BigDecimal tripFco = new BigDecimal(0);
	public BigDecimal addTripFco(BigDecimal cc) {
		tripFco = tripFco.add(cc);
		return tripFco;
	}
	public BigDecimal getTripFco() {
		return tripFco;
	}

}
