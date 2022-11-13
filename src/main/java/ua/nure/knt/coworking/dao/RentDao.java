package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.entity.RentApplication;

import java.sql.SQLException;
import java.util.List;

public interface RentDao {
	List<RentApplication> readAllRentApplicationByStatus(StatusEnum statusEnum) throws SQLException;
	List<RentApplication> readAllRentApplicationByUser(String userEmail) throws SQLException;
	List<RentApplication> readAllRentApplicationByUserAndStatus(String userEmail, StatusEnum statusEnum) throws SQLException;
	RentApplication readRentApplicationById(Integer id) throws SQLException;
	Integer createRentApplication(RentApplication rentApplication) throws SQLException;
	Integer updateRentApplicationStatus(RentApplication rentApplication) throws SQLException;
}
