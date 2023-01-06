package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.observers.Observable;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PlaceDao extends Observable {
	List<Place> readPlaceByRoomName(String roomName) throws SQLException;
	List<Place> readAvailablePlace(LocalDate dateFrom, LocalDate dateTo, String roomType) throws SQLException;
	List<Place> readAllPlaces() throws SQLException;
	Place readPlaceById(Integer id) throws SQLException;
	Integer createPlace(Place place) throws SQLException;
	Integer updatePlace(Place place) throws SQLException;
	Integer deletePlaceById(Integer id) throws SQLException;
}
