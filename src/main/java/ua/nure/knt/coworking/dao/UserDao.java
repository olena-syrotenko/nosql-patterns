package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.entity.User;

public interface UserDao {
	User readUserByEmail(String email);
	User readUserById(Integer id);
	boolean createUser(User user);
	boolean updateUser(User user);
}
