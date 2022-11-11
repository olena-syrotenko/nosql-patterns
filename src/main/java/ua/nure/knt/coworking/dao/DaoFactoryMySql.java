package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.dao.mysql.UserDaoMySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DaoFactoryMySql extends DaoFactory {
	private static Properties connectionInfo;

	DaoFactoryMySql() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		connectionInfo = new Properties();
		connectionInfo.put("user", "root");
		connectionInfo.put("password", "helen");
		connectionInfo.put("useUnicode", "true");
		connectionInfo.put("characterEncoding", "utf-8");
		connectionInfo.put("serverTimezone", "UTC");
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:mysql://localhost/coworking_db", connectionInfo);
	}

	@Override
	public UserDao getUserDao() throws SQLException {
		return new UserDaoMySql(getConnection());
	}
}
