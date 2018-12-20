package com.druid.monitor.detector.bean;

public class MethodExecuteMonitoringBean extends ExecuteMonitoringBean {

	private String key;
	private String className;
	private String methodName;

	public String getKey() {
		if (className != null && methodName != null) {
			String classKey = className.substring(className.lastIndexOf(".") + 1, className.length());
			String methodKey = methodName.substring(0, methodName.indexOf("("));
			key = classKey.concat("_").concat(methodKey);
		}
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	@Override
	public String toString() {
		String result = "Class Name=" + this.className + ", Method=" + this.methodName + super.toString();
		return result;
	}

}