package ua.nure.knt.coworking.dao;

import ua.nure.knt.coworking.entity.Tariff;

import java.sql.SQLException;
import java.util.List;

public interface TariffDao {
	List<Tariff> readAllTariffs() throws SQLException;

	List<Tariff> readTariffByRoomType(String roomTypeName) throws SQLException;

	List<Tariff> readTariffByTimeUnit(String timeUnit) throws SQLException;

	Tariff readTariffById(Integer id) throws SQLException;

	Integer createTariff(Tariff tariff) throws SQLException;

	Integer updateTariff(Tariff tariff) throws SQLException;

	Integer deleteTariffById(Integer id) throws SQLException;

	Integer migrate(List<Tariff> tariffs) throws SQLException;
}
