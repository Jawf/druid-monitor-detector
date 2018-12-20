package com.druid.monitor.detector.model;

import javax.persistence.Entity;

/**
 * The Person entity (a subtype of User).
 */
@Entity
public class Person extends User {

	// Person's first name
	private String firstName;

	// Person's last name
	private String lastName;

	/**
	 * Default Person constructor
	 */
	public Person() {
	}

	/**
	 * Build a new Person with the passed id.
	 */
	public Person(String id) {
		this.setId(id);
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
