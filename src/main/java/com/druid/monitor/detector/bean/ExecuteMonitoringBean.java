package com.druid.monitor.detector.bean;

import java.math.BigDecimal;

public class ExecuteMonitoringBean {

	private String key;
	private int executeCount; // 执行次数
	private int executeTimeMillis; // 执行总耗时(ms)
	private int executeErrorCount; // 执行错误数
	private BigDecimal executeCost; // 执行平均耗时
	private boolean hasExcceed10s; // 执行超过100s
	private boolean hasExcceed100s; // 执行超过1000s
	private boolean hasExcceed1000s; // 执行超过100s
	private String LastErrorTime; //最近一次出错时间
	private String lastErrorMessage; //最近一次出错Error Stack

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getExecuteCount() {
		return executeCount;
	}

	public void setExecuteCount(int executeCount) {
		this.executeCount = executeCount;
	}

	public int getExecuteTimeMillis() {
		return executeTimeMillis;
	}

	public void setExecuteTimeMillis(int executeTimeMillis) {
		this.executeTimeMillis = executeTimeMillis;
	}

	public int getExecuteErrorCount() {
		return executeErrorCount;
	}

	public void setExecuteErrorCount(int executeErrorCount) {
		this.executeErrorCount = executeErrorCount;
	}

	public BigDecimal getExecuteCost() {
		return executeCost;
	}

	public void setExecuteCost(BigDecimal executeCost) {
		this.executeCost = executeCost;
	}

	public boolean getHasExcceed10s() {
		return hasExcceed10s;
	}

	public void setHasExcceed10s(boolean hasExcceed10s) {
		this.hasExcceed10s = hasExcceed10s;
	}

	public boolean getHasExcceed100s() {
		return hasExcceed100s;
	}

	public void setHasExcceed100s(boolean hasExcceed100s) {
		this.hasExcceed100s = hasExcceed100s;
	}

	public boolean getHasExcceed1000s() {
		return hasExcceed1000s;
	}

	public void setHasExcceed1000s(boolean hasExcceed1000s) {
		this.hasExcceed1000s = hasExcceed1000s;
	}

	public String getLastErrorTime() {
		return LastErrorTime;
	}

	public void setLastErrorTime(String lastErrorTime) {
		LastErrorTime = lastErrorTime;
	}

	public String getLastErrorMessage() {
		return lastErrorMessage;
	}

	public void setLastErrorMessage(String lastErrorMessage) {
		this.lastErrorMessage = lastErrorMessage;
	}

	@Override
	public String toString() {
		String result = " [Average Execute Cost=" + this.executeCost + "(s), Total Execute Count="
				+ this.executeCount + ", Total Execute Time in Millis=" + this.getExecuteTimeMillis()
				+ "(ms), Error Execute Count=" + this.getExecuteErrorCount() + ", Has Execution Excceed 10s="
				+ this.hasExcceed10s + ", Has Execution Excceed 100s=" + this.hasExcceed100s
				+ ", Has Execution Excceed 1000s=" + this.hasExcceed1000s + ", Latest Error Time=" 
				+ this.LastErrorTime + ", Error With Stack Trace=<" + this.lastErrorMessage + ">]";
		return result;
	}
}