package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;

import java.sql.SQLException;
import java.util.List;

public class UserService {
	private final UserDao userDao;

	public UserService(DaoFactory daoFactory) throws SQLException {
		userDao = DaoFactory.getDaoFactory(DaoType.MySQL)
				.getUserDao();
	}

	List<User> readAllUsers() throws SQLException {
		return userDao.readAllUsers();
	}

	void saveUser(User user) throws SQLException {
		userDao.createUser(user);
	}

	void updateUser(User user) throws SQLException {
		userDao.updateUser(user);
	}
}
