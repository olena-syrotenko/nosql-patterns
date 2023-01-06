package ua.nure.knt.coworking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.constants.RoleEnum;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.User;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.observers.LoggerObserver;
import ua.nure.knt.coworking.util.RoleBuilder;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserService {
	private final DaoFactory daoFactory;

	@Autowired
	public UserService() {
		daoFactory = DaoFactory.getDaoFactory(DaoType.MySQL);
	}

	public List<User> readAllUsers() {
		try {
			return daoFactory.getUserDao()
					.readAllUsers();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public User readUserByEmail(String email) {
		try {
			return daoFactory.getUserDao()
					.readUserByEmail(email);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public User readUserById(Integer id) {
		try {
			return daoFactory.getUserDao()
					.readUserById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<User> readUserByFullName(String fullName) {
		try {
			String[] name = fullName.split(" ");
			return daoFactory.getUserDao()
					.readUsersByFullName(name[0], name[1]);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveUser(User user, ContentObserver contentObserver) {
		try {
			user.setRole(new RoleBuilder().setName(RoleEnum.ROLE_USER.name())
					.build());
			UserDao userDao = daoFactory.getUserDao();
			userDao.attach(new LoggerObserver());
			userDao.attach(contentObserver);
			userDao.createUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateUser(User user, ContentObserver contentObserver) {
		try {
			UserDao userDao = daoFactory.getUserDao();
			userDao.attach(new LoggerObserver());
			userDao.attach(contentObserver);
			userDao.updateUser(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
