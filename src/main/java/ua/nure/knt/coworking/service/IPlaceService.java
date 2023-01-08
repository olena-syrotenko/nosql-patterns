package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.observers.ContentObserver;

import java.util.List;

public interface IPlaceService {
	Place readPlaceById(Integer id);

	List<Place> readAllPlaces();

	List<Place> readAvailablePlaces(String rentPeriod);

	void savePlace(Place place, String role, ContentObserver contentObserver);

	void updatePlace(Place place, String role, ContentObserver contentObserver);

	void deletePlace(Integer id, String role, ContentObserver contentObserver);
}
