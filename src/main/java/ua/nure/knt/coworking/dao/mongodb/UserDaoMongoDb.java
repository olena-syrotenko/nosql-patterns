package ua.nure.knt.coworking.dao.mongodb;

import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;

public class UserDaoMongoDb implements UserDao {
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
