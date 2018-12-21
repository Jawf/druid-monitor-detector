package com.druid.monitor.detector.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.druid.stat.DruidStatService;
import com.alibaba.druid.support.json.JSONUtils;
import com.druid.monitor.detector.bean.MethodExecuteMonitoringBean;
import com.druid.monitor.detector.bean.SqlExecuteMonitoringBean;

/**
 * Component layer is below Controller and above Service.
 * 
 *
 */
@Component
public class BadCodeDetectorComponent {
	private static final Logger LOGGER = LoggerFactory.getLogger(BadCodeDetectorComponent.class);

	public void monitorAndDetectBadMethodExcution() {
		LOGGER.debug("monitorAndDetectBadMethodExcution-execute...");
		Map<String, MethodExecuteMonitoringBean> suspectedMethodList = new HashMap<String, MethodExecuteMonitoringBean>();
		String result = DruidStatService.getInstance()
				.service("/spring.json?orderBy=ExecuteTimeMillis&orderType=desc&page=1&perPageCount=1000");
		suspectedMethodList = processWarningMethodDataList(result, suspectedMethodList);
		LOGGER.debug("monitorAndDetectBadMethodExcution-executed query and result process.1..");

		// 1-10s: /spring.json?orderBy=Histogram[4]&orderType=desc&page=1&perPageCount=1000
		result = DruidStatService.getInstance()
				.service("/spring.json?orderBy=Histogram[4]&orderType=desc&page=1&perPageCount=1000");
		suspectedMethodList = processWarningMethodDataList(result, suspectedMethodList);
		LOGGER.debug("monitorAndDetectBadMethodExcution-executed query and result process.2..");

		// 10-100s: /spring.json?orderBy=Histogram[5]&orderType=desc&page=1&perPageCount=100
		result = DruidStatService.getInstance()
				.service("/spring.json?orderBy=Histogram[5]&orderType=desc&page=1&perPageCount=100");
		suspectedMethodList = processWarningMethodDataList(result, suspectedMethodList);
		LOGGER.debug("monitorAndDetectBadMethodExcution-executed query and result process.3..");

		// 100-1000s: /spring.json?orderBy=Histogram[6]&orderType=desc&page=1&perPageCount=10
		result = DruidStatService.getInstance()
				.service("/spring.json?orderBy=Histogram[6]&orderType=desc&page=1&perPageCount=10");
		suspectedMethodList = processWarningMethodDataList(result, suspectedMethodList);
		LOGGER.debug("monitorAndDetectBadMethodExcution-executed query and result process.4..");

		// >1000s: /spring.json?orderBy=Histogram[7]&orderType=desc&page=1&perPageCount=5
		result = DruidStatService.getInstance()
				.service("/spring.json?orderBy=Histogram[7]&orderType=desc&page=1&perPageCount=5");
		suspectedMethodList = processWarningMethodDataList(result, suspectedMethodList);
		LOGGER.debug("monitorAndDetectBadMethodExcution-executed query and result process.5..");

		// send notification
		if (suspectedMethodList != null && !suspectedMethodList.isEmpty() && suspectedMethodList.get(0) != null) {
			String errorText = "";
			for (MethodExecuteMonitoringBean bean : suspectedMethodList.values()) {
				errorText = bean.toString() + "<br/>";
			}

			sendSystemWarningAsBadMethodExcutionDetectedNotification(suspectedMethodList.get(0).getKey(),
					suspectedMethodList.get(0).getClassName(), suspectedMethodList.get(0).getMethodName(),
					suspectedMethodList.get(0).getExecuteCost(), suspectedMethodList.get(0).getExecuteErrorCount(),
					errorText);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Map<String, MethodExecuteMonitoringBean> processWarningMethodDataList(String result,
			Map<String, MethodExecuteMonitoringBean> suspectedMethodList) {
		Map<String, Object> resultMap = (Map<String, Object>) JSONUtils.parse(result);
		List<Map<String, Object>> contentList = (List<Map<String, Object>>) resultMap.get("Content");
		if (contentList != null && !contentList.isEmpty()) {
			for (Map<String, Object> contentMap : contentList) {
				String className = (String) contentMap.get("Class");
				if (className == null || "".equals(className)) {
					continue;
				}
				String methodName = (String) contentMap.get("Method");
				int executeCount = parseInt(contentMap.get("ExecuteCount"));
				int executeTimeMillis = parseInt(contentMap.get("ExecuteTimeMillis"));
				int executeErrorCount = parseInt(contentMap.get("ExecuteErrorCount"));
				List histogram = (List) contentMap.get("Histogram");
				String lastErrorTime = contentMap.get("LastErrorTime") != null
						? contentMap.get("LastErrorTime").toString() : "";
				String lastErrorMessage = contentMap.get("LastError") != null ? contentMap.get("LastError").toString()
						: "";

				BigDecimal avgExecuteCostTime = new BigDecimal(executeTimeMillis)
						.divide(new BigDecimal(executeCount), 4, RoundingMode.HALF_UP)
						.divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);

				MethodExecuteMonitoringBean bean = new MethodExecuteMonitoringBean();
				bean.setClassName(className);
				bean.setMethodName(methodName);
				bean.setExecuteTimeMillis(executeTimeMillis);
				bean.setExecuteCount(executeCount);
				bean.setExecuteErrorCount(executeErrorCount);
				BigDecimal cost = avgExecuteCostTime;
				cost = cost.setScale(2, RoundingMode.HALF_UP);
				bean.setExecuteCost(cost);
				bean.setLastErrorTime(lastErrorTime);
				bean.setLastErrorMessage(lastErrorMessage);
				if (histogram != null) {
					if ((int) histogram.get(5) > 0) {
						bean.setHasExcceed10s(true);
					}
					if ((int) histogram.get(6) > 0) {
						bean.setHasExcceed100s(true);
					}
					if ((int) histogram.get(7) > 0) {
						bean.setHasExcceed1000s(true);
					}
				}
				if (avgExecuteCostTime.compareTo(new BigDecimal(3)) > 0 || executeErrorCount > 0) {
					LOGGER.debug(
							"processWarningMethodDataList-method execute status-WARNING! Error Method Count or Cost execution found with className={}, method={}, avgExecuteTime={}, executeCount={}, executeTimeMillis={}, executeErrorCount={}",
							className, methodName, avgExecuteCostTime, executeCount, executeTimeMillis,
							executeErrorCount);
					LOGGER.error(
							"processWarningMethodDataList-method execute status-WARNING! Error Method Count or Cost execution found with className={}, method={}, avgExecuteTime={}, executeCount={}, executeTimeMillis={}, executeErrorCount={}",
							className, methodName, avgExecuteCostTime, executeCount, executeTimeMillis,
							executeErrorCount);

					suspectedMethodList.put(bean.getKey(), bean);
				} else if (bean.getHasExcceed10s() || bean.getHasExcceed100s() || bean.getHasExcceed1000s()) {
					suspectedMethodList.put(bean.getKey(), bean);
				}
			}
		}
		return suspectedMethodList;
	}

	public void sendSystemWarningAsBadMethodExcutionDetectedNotification(String methodKey, String className,
			String methodName, BigDecimal executeCost, int executeErrorCount, String text) {
		// TODO send notification
	}

	private int parseInt(Object obj) {
		int result = 0;
		if (obj != null) {
			try {
				result = Integer.parseInt(String.valueOf(obj));
			} catch (Exception e) {
				// Do nothing
			}
		}
		return result;
	}

	public void monitorAndDetectBadSqlExcution() {
		LOGGER.debug("monitorAndDetectBadSqlExcution-execute...");
		Map<String, SqlExecuteMonitoringBean> suspectedSqlList = new HashMap<String, SqlExecuteMonitoringBean>();
		String result = DruidStatService.getInstance()
				.service("/sql.json?orderBy=TotalTime&orderType=desc&page=1&perPageCount=1000");
		suspectedSqlList = processWarningSqlDataList(result, suspectedSqlList);
		LOGGER.debug("monitorAndDetectBadSqlExcution-executed query and result process.1..");

		// 1-10s: /sql.json?orderBy=Histogram[4]&orderType=desc&page=1&perPageCount=1000
		result = DruidStatService.getInstance()
				.service("/sql.json?orderBy=Histogram[4]&orderType=desc&page=1&perPageCount=1000");
		suspectedSqlList = processWarningSqlDataList(result, suspectedSqlList);
		LOGGER.debug("monitorAndDetectBadSqlExcution-executed query and result process.2..");

		// 10-100s: /sql.json?orderBy=Histogram[5]&orderType=desc&page=1&perPageCount=100
		result = DruidStatService.getInstance()
				.service("/sql.json?orderBy=Histogram[5]&orderType=desc&page=1&perPageCount=100");
		suspectedSqlList = processWarningSqlDataList(result, suspectedSqlList);
		LOGGER.debug("monitorAndDetectBadSqlExcution-executed query and result process.3..");

		// 100-1000s: /sql.json?orderBy=Histogram[6]&orderType=desc&page=1&perPageCount=10
		result = DruidStatService.getInstance()
				.service("/sql.json?orderBy=Histogram[5]&orderType=desc&page=1&perPageCount=10");
		suspectedSqlList = processWarningSqlDataList(result, suspectedSqlList);
		LOGGER.debug("monitorAndDetectBadSqlExcution-executed query and result process.4..");

		// >1000s: /sql.json?orderBy=Histogram[7]&orderType=desc&page=1&perPageCount=5
		result = DruidStatService.getInstance()
				.service("/spring.json?orderBy=Histogram[7]&orderType=desc&page=1&perPageCount=5");
		suspectedSqlList = processWarningSqlDataList(result, suspectedSqlList);
		LOGGER.debug("monitorAndDetectBadSqlExcution-executed query and result process.5..");

		// send notification
		if (suspectedSqlList != null && !suspectedSqlList.isEmpty() && suspectedSqlList.get(0) != null) {
			String errorText = "";
			for (SqlExecuteMonitoringBean bean : suspectedSqlList.values()) {
				errorText = bean.toString() + "<br/>";
			}

			sendSystemWarningAsBadSqlExcutionDetectedNotification(suspectedSqlList.get(0).getKey(),
					suspectedSqlList.get(0).getSql(), suspectedSqlList.get(0).getExecuteCost(),
					suspectedSqlList.get(0).getExecuteErrorCount(), errorText);
		}
	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	private Map<String, SqlExecuteMonitoringBean> processWarningSqlDataList(String result,
			Map<String, SqlExecuteMonitoringBean> suspectedMethodList) {
		Map<String, Object> resultMap = (Map<String, Object>) JSONUtils.parse(result);
		List<Map<String, Object>> contentList = (List<Map<String, Object>>) resultMap.get("Content");
		if (contentList != null && !contentList.isEmpty()) {
			for (Map<String, Object> contentMap : contentList) {
				String sql = (String) contentMap.get("SQL");
				if (sql == null || "".equals(sql)) {
					continue;
				}
				int executeCount = parseInt(contentMap.get("ExecuteCount"));
				int executeTimeMillis = parseInt(contentMap.get("TotalTime"));
				int executeErrorCount = parseInt(contentMap.get("ErrorCount"));
				List histogram = (List) contentMap.get("Histogram");
				String lastErrorTime = contentMap.get("LastErrorTime") != null
						? contentMap.get("LastErrorTime").toString() : "";
				String lastErrorMessage = contentMap.get("LastError") != null ? contentMap.get("LastError").toString()
						: "";

				BigDecimal avgExecuteCostTime = new BigDecimal(executeTimeMillis)
						.divide(new BigDecimal(executeCount), 4, RoundingMode.HALF_UP)
						.divide(new BigDecimal(1000), 2, RoundingMode.HALF_UP);
				SqlExecuteMonitoringBean bean = new SqlExecuteMonitoringBean();
				bean.setSql(sql);
				bean.setExecuteTimeMillis(executeTimeMillis);
				bean.setExecuteCount(executeCount);
				bean.setExecuteErrorCount(executeErrorCount);
				BigDecimal cost = avgExecuteCostTime;
				cost = cost.setScale(2, RoundingMode.HALF_UP);
				bean.setExecuteCost(cost);
				bean.setLastErrorTime(lastErrorTime);
				bean.setLastErrorMessage(lastErrorMessage);
				if (histogram != null) {
					if ((int) histogram.get(5) > 0) {
						bean.setHasExcceed10s(true);
					}
					if ((int) histogram.get(6) > 0) {
						bean.setHasExcceed100s(true);
					}
					if ((int) histogram.get(7) > 0) {
						bean.setHasExcceed1000s(true);
					}
				}
				if (avgExecuteCostTime.compareTo(new BigDecimal(3)) > 0 || executeErrorCount > 0) {
					LOGGER.debug(
							"processWarningSqlDataList-SQL execute status-WARNING! Error SQL Count or Cost execution found with sql={}, avgExecuteTime={}, executeCount={}, executeTimeMillis={}, executeErrorCount={}",
							sql.substring(0, sql.length() >= 30 ? 29 : sql.length()), executeTimeMillis,
							avgExecuteCostTime, executeCount, executeTimeMillis, executeErrorCount);
					LOGGER.error(
							"processWarningSqlDataList-SQL execute status-WARNING! Error SQL Count or Cost execution found with sql={}, avgExecuteTime={}, executeCount={}, executeTimeMillis={}, executeErrorCount={}",
							sql.substring(0, sql.length() >= 30 ? 29 : sql.length()), avgExecuteCostTime, executeCount,
							executeTimeMillis, executeErrorCount);

					suspectedMethodList.put(bean.getKey(), bean);
				} else if (bean.getHasExcceed10s() || bean.getHasExcceed100s() || bean.getHasExcceed1000s()) {
					suspectedMethodList.put(bean.getKey(), bean);
				}
			}
		}
		return suspectedMethodList;
	}

	public void sendSystemWarningAsBadSqlExcutionDetectedNotification(String methodKey, String sql,
			BigDecimal executeCost, int executeErrorCount, String text) {
		// TODO send notification
	}

	/*
	 * public static void main(String... args) {
	 * MonitorDetectorComponent c = new MonitorDetectorComponent();
	 * Map<String, MethodExecuteMonitoringBean> suspectedMethodList = new HashMap<String,
	 * MethodExecuteMonitoringBean>();
	 * String result = "";
	 * suspectedMethodList = c.processWarningMethodDataList(result, suspectedMethodList);
	 * System.out.println(suspectedMethodList.size());
	 * for (MethodExecuteMonitoringBean bean : suspectedMethodList.values()) {
	 * System.out.println(bean.getMethodName());
	 * System.out.println(bean.getLastErrorMessage());
	 * }
	 * 
	 * Map<String, SqlExecuteMonitoringBean> suspectedSqlList = new HashMap<String, SqlExecuteMonitoringBean>();
	 * String resultSql = "";
	 * suspectedSqlList = c.processWarningSqlDataList(resultSql, suspectedSqlList);
	 * System.out.println(suspectedSqlList.size());
	 * for (SqlExecuteMonitoringBean bean : suspectedSqlList.values()) {
	 * System.out.println(bean.getSql());
	 * System.out.println(bean.getLastErrorMessage());
	 * }
	 * }
	 */

}
