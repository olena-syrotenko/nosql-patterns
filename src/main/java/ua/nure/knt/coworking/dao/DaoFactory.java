package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.dao.mongodb.DaoFactoryMongoDb;
import ua.nure.knt.coworking.dao.mysql.DaoFactoryMySql;

import java.sql.SQLException;

public abstract class DaoFactory {
	private static DaoFactory daoFactory = null;

	public static DaoFactory getDaoFactory(DaoType daoType){
		if(daoFactory == null){
			daoFactory = switch (daoType){
				case MySQL -> new DaoFactoryMySql();
				case MongoDB -> new DaoFactoryMongoDb();
			};
		}
		return daoFactory;
	}

	public abstract UserDao getUserDao() throws SQLException;
	public abstract PlaceDao getPlaceDao() throws SQLException;
	public abstract TariffDao getTariffDao() throws SQLException;
}
