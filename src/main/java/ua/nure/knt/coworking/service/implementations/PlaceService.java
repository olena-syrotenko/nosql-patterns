package ua.nure.knt.coworking.service.implementations;

import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.PlaceDao;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.observers.LoggerObserver;
import ua.nure.knt.coworking.service.IPlaceService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Service
public class PlaceService implements IPlaceService {
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

	public void savePlace(Place place, String role, ContentObserver contentObserver) {
		try {
			PlaceDao placeDao = daoFactory.getPlaceDao();
			placeDao.attach(new LoggerObserver());
			placeDao.attach(contentObserver);
			placeDao.createPlace(place);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updatePlace(Place place, String role, ContentObserver contentObserver) {
		try {
			PlaceDao placeDao = daoFactory.getPlaceDao();
			placeDao.attach(new LoggerObserver());
			placeDao.attach(contentObserver);
			placeDao.updatePlace(place);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deletePlace(Integer id, String role, ContentObserver contentObserver) {
		try {
			PlaceDao placeDao = daoFactory.getPlaceDao();
			placeDao.attach(new LoggerObserver());
			placeDao.attach(contentObserver);
			placeDao.deletePlaceById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
