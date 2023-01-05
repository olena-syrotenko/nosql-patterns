package ua.nure.knt.coworking.service;

import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.entity.Place;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlaceService {
	private final DaoFactory daoFactory;

	public PlaceService() {
		daoFactory = DaoFactory.getDaoFactory(DaoType.MySQL);
	}

	public Place readPlaceById(Integer id) {
		try {
			return daoFactory.getPlaceDao()
					.readPlaceById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Place> readAllPlaces() {
		try {
			return daoFactory.getPlaceDao()
					.readAllPlaces();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Place> readAvailablePlaces(String rentPeriod) {
		try {
			String[] rentInfo = rentPeriod.split(" ");
			return daoFactory.getPlaceDao()
					.readAvailablePlace(LocalDate.parse(rentInfo[0]), LocalDate.parse(rentInfo[1]), rentInfo[2].replaceAll("_", " "));
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void savePlace(Place place) {
		try {
			daoFactory.getPlaceDao()
					.createPlace(place);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePlace(Place place) {
		try {
			daoFactory.getPlaceDao()
					.updatePlace(place);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deletePlace(Integer id) {
		try {
			daoFactory.getPlaceDao()
					.deletePlaceById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
