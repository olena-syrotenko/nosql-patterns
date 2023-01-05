package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.entity.Tariff;

import java.sql.SQLException;
import java.util.List;

public class TariffService {
	private final TariffDao tariffDao;

	public TariffService(DaoFactory daoFactory) throws SQLException {
		tariffDao = DaoFactory.getDaoFactory(DaoType.MySQL)
				.getTariffDao();
	}

	List<Tariff> readAllTariffs() throws SQLException {
		return tariffDao.readAllTariffs();
	}

	void saveTariff(Tariff user) throws SQLException {
		tariffDao.createTariff(user);
	}

	void updateTariff(Tariff user) throws SQLException {
		tariffDao.updateTariff(user);
	}

	void deleteTariff(Integer id) throws SQLException {
		tariffDao.deleteTariffById(id);
	}
}
