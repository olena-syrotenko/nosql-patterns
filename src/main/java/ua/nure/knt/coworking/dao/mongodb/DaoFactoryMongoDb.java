package ua.nure.knt.coworking.dao.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.dao.UserDao;

import java.sql.SQLException;

public class DaoFactoryMongoDb extends DaoFactory {
	private static final String URI = "mongodb://localhost:27017";

	private static MongoClient getMongoClient() {
		return MongoClients.create(URI);
	}

	@Override
	public UserDao getUserDao() {
		return null;
	}

	@Override
	public PlaceDao getPlaceDao() throws SQLException {
		return new PlaceDaoMongoDb(getMongoClient());
	}

	@Override
	public TariffDao getTariffDao() throws SQLException {
		return null;
	}

	@Override
	public RentDao getRentDao() throws SQLException {
		return null;
	}
}
