package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Role;
import ua.nure.knt.coworking.entity.User;

public class UserBuilder implements Builder<User> {
	private Integer id;
	private String email;
	private String password;
	private String lastName;
	private String firstName;
	private String passportId;
	private String phoneNumber;
	private Role role;

	public UserBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public UserBuilder setEmail(String email) {
		this.email = email;
		return this;
	}

	public UserBuilder setPassword(String password) {
		this.password = password;
		return this;
	}

	public UserBuilder setLastName(String lastName) {
		this.lastName = lastName;
		return this;
	}

	public UserBuilder setFirstName(String firstName) {
		this.firstName = firstName;
		return this;
	}

	public UserBuilder setPassportId(String passportId) {
		this.passportId = passportId;
		return this;
	}

	public UserBuilder setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}

	public UserBuilder setRole(Role role) {
		this.role = role;
		return this;
	}

	@Override
	public User build() {
		User user = new User();
		user.setId(this.id);
		user.setEmail(this.email);
		user.setPassword(this.password);
		user.setFirstName(this.firstName);
		user.setLastName(this.lastName);
		user.setPassportId(this.passportId);
		user.setPhoneNumber(this.phoneNumber);
		user.setRole(this.role);
		return user;
	}
}
