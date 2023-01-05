package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.entity.Place;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class PlaceService {
	private final PlaceDao placeDao;

	public PlaceService(DaoFactory daoFactory) throws SQLException {
		placeDao = DaoFactory.getDaoFactory(DaoType.MySQL)
				.getPlaceDao();
	}

	List<Place> readAllPlaces() throws SQLException {
		return placeDao.readAllPlaces();
	}

	List<Place> readAvailablePlaces(LocalDate startRent, LocalDate endRent, Integer roomType) throws SQLException {
		return placeDao.readAvailablePlace(startRent, endRent, roomType);
	}

	void savePlace(Place place) throws SQLException {
		placeDao.createPlace(place);
	}

	void updatePlace(Place place) throws SQLException {
		placeDao.updatePlace(place);
	}

	void deletePlace(Integer id) throws SQLException {
		placeDao.deletePlaceById(id);
	}
}
