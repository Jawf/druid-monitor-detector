package com.druid.monitor.detector.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.druid.monitor.detector.service.DetectService;

/**
 * ServerStatusController
 */
@Controller
@RequestMapping("/serverstatus")
public class ServerStatusController {

	@Autowired
	private DetectService detectService;

	/**
	 * getServerStatus
	 *
	 * @return ServerStatus
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Object> getServerStatus() {
		detectService.findPerson();
		Map<String, Object> status = new HashMap<String, Object>();
		status.put("status", 1);
		return status;
	}
}
