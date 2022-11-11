package ua.nure.knt.coworking.dao;

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

	abstract UserDao getUserDao() throws SQLException;
}
