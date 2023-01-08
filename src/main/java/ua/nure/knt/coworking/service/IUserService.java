package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.observers.ContentObserver;

import java.util.List;

public interface IUserService {
	List<User> readAllUsers();

	User readUserByEmail(String email);

	User readUserById(Integer id);

	List<User> readUserByFullName(String fullName);

	void saveUser(User user, ContentObserver contentObserver);

	void updateUser(User user, ContentObserver contentObserver);
}
