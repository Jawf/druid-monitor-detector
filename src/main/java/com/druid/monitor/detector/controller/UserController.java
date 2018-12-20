package com.druid.monitor.detector.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.druid.monitor.detector.model.User;
import com.druid.monitor.detector.service.UserService;

/**
 * Controller class for testing user's repositories classes.
 *
 * @author netgloo
 */
@Controller
public class UserController {

	// ==============
	// PRIVATE FIELDS
	// ==============

	@Autowired
	private UserService userService;

	// ==============
	// PUBLIC METHODS
	// ==============

	/**
	 * For testing convenient, used GET method to create: <br/>
	 * /user/person?email=[email]&firstName=[firstName] -> create a new
	 * person user and save it in the database.
	 * 
	 * @param email The person's email
	 * @param firstName The person's first name
	 * @return a string describing if the person is succesfully created or not.
	 */
	@RequestMapping("/user/person")
	@ResponseBody
	public String createPerson(String email, String firstName) {
		try {
			userService.createUser(email, firstName);
		} catch (Exception ex) {
			return "Error creating the person: " + ex.toString();
		}
		return "Person succesfully created!";
	}

	/**
	 * For testing convenient, used GET method to delete: <br/>
	 * 
	 * /user/deletion?id=[id] -> delete the user having the passed id.
	 * 
	 * @param id The id for the user to delete
	 * @return A string describing if the user is succesfully deleted or not.
	 */
	@RequestMapping("/user/deletion")
	@ResponseBody
	public String deleteUser(long id) {
		userService.deleteUser(id);
		return "User succesfully deleted!";
	}

	/**
	 * 
	 * /user/byemail?email=[email] -> return the user having the passed email.
	 * 
	 * @param email The email to search in the database.
	 * @return The user id or a message error if the user is not found.
	 */
	@RequestMapping("/user/byemail")
	@ResponseBody
	public User getUser(String email) {
		User user = userService.findUserByEmail(email);
		return user;
	}

	@RequestMapping("/users")
	@ResponseBody
	public Object getUsers() {
		List<User> users = userService.findUserByQuery();
		return users;
	}

	/**
	 * For testing convenient, used GET method to update: <br/>
	 * /user/updation?id=[id]&email=[email]&name=[name] -> get the user with passed
	 * id and change its email and name (the firstName if the user is of type
	 * Person).
	 * 
	 * @param id The id of the user to update.
	 * @param email The new email value.
	 * @param name The new name for the user.
	 * @return A string describing if the user is succesfully updated or not.
	 */
	@RequestMapping("/user/updation")
	@ResponseBody
	public String update(Long id, String email, String name) {
		try {
			User user = userService.findUserById(id);
			if (user != null) {
				user.setEmail(email);
				// switch on the user type
				userService.updateUser(user);
			}
		} catch (Exception ex) {
			return "Error: " + ex.toString();
		}
		return "User successfully updated.";
	}

} // class UserController
