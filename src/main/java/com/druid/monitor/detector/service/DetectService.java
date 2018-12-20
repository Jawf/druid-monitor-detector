package com.druid.monitor.detector.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.druid.monitor.detector.model.User;
import com.druid.monitor.detector.model.UserRepository;


/**
 * DetectService
 */
@Service
public class DetectService {
	
	  @Autowired
	  private UserRepository userRepository;

	  public Iterable<User> findPerson(){
		  return userRepository.findAll();
	  }
}
