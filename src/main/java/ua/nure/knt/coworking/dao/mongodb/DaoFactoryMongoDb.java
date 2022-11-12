package ua.nure.knt.coworking.dao.mongodb;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.dao.UserDao;

import java.sql.SQLException;

public class DaoFactoryMongoDb extends DaoFactory {
	@Override
	public UserDao getUserDao() {
		return null;
	}

	@Override
	public PlaceDao getPlaceDao() throws SQLException {
		return null;
	}
}
