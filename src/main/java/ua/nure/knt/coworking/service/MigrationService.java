package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.dao.UserDao;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.entity.User;

import java.sql.SQLException;
import java.util.List;

public class MigrationService {
	public static Integer migrationData(DaoFactory migrationFrom, DaoFactory migrationTo) throws SQLException {
		Integer migratedRecords = 0;
		migratedRecords += migrateUsers(migrationFrom.getUserDao(), migrationTo.getUserDao());
		migratedRecords += migrateTariffs(migrationFrom.getTariffDao(), migrationTo.getTariffDao());
		migratedRecords += migratePlaces(migrationFrom.getPlaceDao(), migrationTo.getPlaceDao());
		migratedRecords += migrateRents(migrationFrom.getRentDao(), migrationTo.getRentDao());
		return migratedRecords;
	}

	private static Integer migrateUsers(UserDao migrationFromDao, UserDao migrationToDao) throws SQLException {
		List<User> users = migrationFromDao.readAllUsers();
		for (User user : users) {
			migrationToDao.createUser(user);
		}
		return users.size();
	}

	private static Integer migrateTariffs(TariffDao migrationFromDao, TariffDao migrationToDao) throws SQLException {
		List<Tariff> tariffs = migrationFromDao.readAllTariffs();
		for (Tariff tariff : tariffs) {
			migrationToDao.createTariff(tariff);
		}
		return tariffs.size();
	}

	private static Integer migratePlaces(PlaceDao migrationFromDao, PlaceDao migrationToDao) throws SQLException {
		List<Place> places = migrationFromDao.readAllPlaces();
		for (Place place : places) {
			migrationToDao.createPlace(place);
		}
		return places.size();
	}

	private static Integer migrateRents(RentDao migrationFromDao, RentDao migrationToDao) throws SQLException {
		List<RentApplication> rentApplications = migrationFromDao.readAllRentApplication();
		for (RentApplication rentApplication : rentApplications) {
			migrationToDao.createRentApplication(rentApplication);
		}
		return rentApplications.size();
	}
}
