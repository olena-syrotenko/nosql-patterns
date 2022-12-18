package ua.nure.knt.coworking.dao;

import org.bson.Document;
import ua.nure.knt.coworking.entity.Tariff;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface TariffDao {
	List<Tariff> readAllTariffs() throws SQLException;

	List<Tariff> readTariffByRoomType(String roomTypeName) throws SQLException;

	List<Tariff> readTariffByTimeUnit(String timeUnit) throws SQLException;

	List<Document> readRoomTypeCountByPriceRange(Double maxPrice);

	List<Document> readRoomTypeSumByTimeUnit(String roomType);

	List<Document> readServiceCountByRange(Integer minServiceUsage);

	Document readTimeUnitWithMaxAvgPrice();

	List<Document> readMaxPriceByRoomTypeTimeUnitByServiceNumber(Integer serviceNumber);

	Tariff readTariffById(Integer id) throws SQLException;

	Integer createTariff(Tariff tariff) throws SQLException;

	Integer updateTariff(Tariff tariff) throws SQLException;

	Integer deleteTariffById(Integer id) throws SQLException;
}
