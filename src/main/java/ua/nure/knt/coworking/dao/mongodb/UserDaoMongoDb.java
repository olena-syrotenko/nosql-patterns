package ua.nure.knt.coworking.dao.mongodb;

import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;

import java.util.List;

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
	public List<User> readAllUsers() {
		return null;
	}

	@Override
	public int createUser(User user) {
		return 0;
	}

	@Override
	public int updateUser(User user) {
		return 0;
	}
}
