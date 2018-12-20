package com.druid.monitor.detector.bean;

public class SqlExecuteMonitoringBean extends ExecuteMonitoringBean {

	private String key;
	private String sql;

	public String getKey() {
		if (sql != null) {
			String sKey = sql.substring(0, sql.length() >= 38 ? 37 : sql.length());
			key = sKey.replaceAll("[',\\.\\(\\)\\s]", "");
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	@Override
	public String toString() {
		String result = "SQL=" + sql + super.toString();
		return result;
	}
}