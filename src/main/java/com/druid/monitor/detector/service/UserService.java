package com.druid.monitor.detector.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.druid.monitor.detector.model.Person;
import com.druid.monitor.detector.model.User;
import com.druid.monitor.detector.model.UserRepository;

/**
 * DetectService
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findUserById(long id) {
		Optional<User> userOp = userRepository.findById(id);
		if (userOp.isPresent()) {
			return userOp.get();
		} else {
			return null;
		}
	}

	public List<User> findUserByQuery() {
		return userRepository.findUserByQuery();
	}

	public User findUserByEmail(String email) {
		return userRepository.findFirstByEmail(email);
	}

	public String createUser(String email, String name) {
		try {
			Person user = new Person();
			user.setEmail(email);
			user.setFirstName(name);
			userRepository.save(user);
		} catch (Exception ex) {
			return "Error creating the company: " + ex.toString();
		}
		return "User succesfully created!";
	}

	public String updateUser(User user) {
		try {
			userRepository.save(user);
		} catch (Exception ex) {
			return "Error creating the company: " + ex.toString();
		}
		return "User succesfully updated!";
	}

	public void deleteUser(long id) {
		userRepository.deleteById(id);
	}
}
