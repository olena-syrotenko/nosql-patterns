package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.entity.User;

import java.sql.SQLException;
import java.util.List;

public class MigrationService {
	public static Integer migrateData(DaoFactory migrationFrom, DaoFactory migrationTo) throws SQLException {
		Integer migratedRecords = 0;
		migratedRecords += migrateUsers(migrationFrom, migrationTo);
		migratedRecords += migrateTariffs(migrationFrom, migrationTo);
		migratedRecords += migratePlaces(migrationFrom, migrationTo);
		migratedRecords += migrateRents(migrationFrom, migrationTo);
		return migratedRecords;
	}

	private static Integer migrateUsers(DaoFactory migrationFrom, DaoFactory migrationTo) throws SQLException {
		List<User> users = migrationFrom.getUserDao()
				.readAllUsers();
		for (User user : users) {
			migrationTo.getUserDao()
					.createUser(user);
		}
		return users.size();
	}

	private static Integer migrateTariffs(DaoFactory migrationFrom, DaoFactory migrationTo) throws SQLException {
		List<Tariff> tariffs = migrationFrom.getTariffDao()
				.readAllTariffs();
		for (Tariff tariff : tariffs) {
			migrationTo.getTariffDao()
					.createTariff(tariff);
		}
		return tariffs.size();
	}

	private static Integer migratePlaces(DaoFactory migrationFrom, DaoFactory migrationTo) throws SQLException {
		List<Place> places = migrationFrom.getPlaceDao()
				.readAllPlaces();
		for (Place place : places) {
			migrationTo.getPlaceDao()
					.createPlace(place);
		}
		return places.size();
	}

	private static Integer migrateRents(DaoFactory migrationFrom, DaoFactory migrationTo) throws SQLException {
		List<RentApplication> rentApplications = migrationFrom.getRentDao()
				.readAllRentApplication();
		for (RentApplication rentApplication : rentApplications) {
			migrationTo.getRentDao()
					.createRentApplication(rentApplication);
		}
		return rentApplications.size();
	}
}
