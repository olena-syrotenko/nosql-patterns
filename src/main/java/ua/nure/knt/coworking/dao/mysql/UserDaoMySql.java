package ua.nure.knt.coworking.dao.mysql;

import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;

import java.sql.Connection;

public class UserDaoMySql implements UserDao {
	private final Connection connection;

	public UserDaoMySql(Connection connection) {
		this.connection = connection;
	}

	@Override
	public User readUserByEmail(String email) {
		return null;
	}

	@Override
	public User readUserById(Integer id) {
		return null;
	}

	@Override
	public boolean createUser(User user) {
		return false;
	}

	@Override
	public boolean updateUser(User user) {
		return false;
	}
}
