package com.druid.monitor.detector.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;

@Configuration
@SuppressWarnings({"unchecked", "rawtypes"})
public class DruidMonitorConfig {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DruidMonitorConfig.class);

	/**
	 * Use this instance address can enable only one instance if distribute deployed in production.
	 * Such as:
	 * application.druid.monitor.enableInstanceAddress=192.168.0.5:8081
	 * can only enable instance with: ip is192.168.0.5 and port is 8081.
	 */
	@Value("${application.druid.monitor.enableInstanceAddress:}")
	private String druidEnableAddress;
	
	@Value("${server.port:8080}")
	private String localPort;
	
	private boolean enabled = false;

	@Bean
	public ServletRegistrationBean druidServlet() {
		ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
				"/druid/*");
		// 白名单：
		// servletRegistrationBean.addInitParameter("allow","127.0.0.1");
		// IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
		// servletRegistrationBean.addInitParameter("deny","192.168.1.73");
		// 登录查看信息的账号密码.
		servletRegistrationBean.addInitParameter("loginUsername", "admin");
		servletRegistrationBean.addInitParameter("loginPassword", "admin");
		// 是否能够重置数据.
		servletRegistrationBean.addInitParameter("resetEnable", "true");
		
		// check enable or disable
		String localIp = "";
		try {
			localIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		String hostAddress = localIp.concat(":").concat(localPort);
		//default enable all instance
		if (StringUtils.isEmpty(druidEnableAddress) || druidEnableAddress.equalsIgnoreCase(hostAddress)) {
			enabled = true;
			LOGGER.info("enabled druid monitor-druidServlet enabled address={}, enabled={}", druidEnableAddress,
					enabled);
		}
		if (!enabled) {
			servletRegistrationBean.setEnabled(false);
		}
		return servletRegistrationBean;
	}
	
	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
		filterRegistrationBean.setFilter(new WebStatFilter());
		filterRegistrationBean.addUrlPatterns("/*");
		filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
		return filterRegistrationBean;
	}

	/**
	 * 添加aop的advice
	 * 
	 */
	@Bean
	public DruidStatInterceptor druidStatInterceptor() {
		DruidStatInterceptor druidStatInterceptor = new DruidStatInterceptor();
		return druidStatInterceptor;
	}

	/**
	 * 添加aop的pointcut
	 * 
	 */
	@Bean
	public JdkRegexpMethodPointcut jdkRegexpMethodPointcut() {
		JdkRegexpMethodPointcut jdkRegexpMethodPointcut = new JdkRegexpMethodPointcut();
		jdkRegexpMethodPointcut.setPatterns(new String[] {"com.*.service.*", "com.*.component.*", "com.*.controller.*"});
		return jdkRegexpMethodPointcut;
	}

	/**
	 * 设置默认的aop配置对应的是原来的<aop:advisor>
	 * 
	 */
	@Bean
	public Advisor druidAdvisor() {
		DefaultPointcutAdvisor defaultPointcutAdvisor = new DefaultPointcutAdvisor();
		defaultPointcutAdvisor.setPointcut(jdkRegexpMethodPointcut());
		defaultPointcutAdvisor.setAdvice(druidStatInterceptor());
		return defaultPointcutAdvisor;
	}

}
