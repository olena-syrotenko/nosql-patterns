package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
	User readUserByEmail(String email) throws SQLException;

	User readUserById(Integer id) throws SQLException;

	List<User> readAllUsers() throws SQLException;

	int createUser(User user) throws SQLException;

	int updateUser(User user) throws SQLException;
}
