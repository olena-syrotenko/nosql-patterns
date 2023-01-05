package ua.nure.knt.coworking.dao;

import org.apache.commons.lang3.tuple.Pair;
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

	Map<String, Long> readRoomTypeCountByPriceRangeWithoutAggregation(Double maxPrice);

	List<Document> readRoomTypeSumByTimeUnit(String roomType);

	Map<String, Double> readRoomTypeSumByTimeUnitWithoutAggregation(String roomType);

	List<Document> readServiceCountByRange(Integer minServiceUsage);

	Map<String, Long> readServiceCountByRangeWithoutAggregation(Integer minServiceUsage);

	Document readTimeUnitWithMaxAvgPrice();

	Map.Entry<String, Double> readTimeUnitWithMaxAvgPriceWithoutAggregation();

	List<Document> readMaxPriceByRoomTypeTimeUnitByServiceNumber(Integer serviceNumber);

	Map<Pair<String, String>, Double> readMaxPriceByRoomTypeTimeUnitByServiceNumberWithoutAggregation(Integer serviceNumber);

	Tariff readTariffById(Integer id) throws SQLException;

	Integer createTariff(Tariff tariff) throws SQLException;

	Integer updateTariff(Tariff tariff) throws SQLException;

	Integer deleteTariffById(Integer id) throws SQLException;
}
