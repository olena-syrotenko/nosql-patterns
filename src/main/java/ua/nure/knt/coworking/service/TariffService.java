package ua.nure.knt.coworking.service;

import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.entity.Tariff;

import java.sql.SQLException;
import java.util.List;

@Service
public class TariffService {
	private final DaoFactory daoFactory;

	public TariffService() {
		daoFactory = DaoFactory.getDaoFactory(DaoType.MySQL);
	}

	public List<Tariff> readAllTariffs() {
		try {
			return daoFactory.getTariffDao()
					.readAllTariffs();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Tariff readTariffById(Integer id) {
		try {
			return daoFactory.getTariffDao()
					.readTariffById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveTariff(Tariff user) {
		try {
			daoFactory.getTariffDao()
					.createTariff(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTariff(Tariff user) {
		try {
			daoFactory.getTariffDao()
					.updateTariff(user);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteTariff(Integer id) {
		try {
			daoFactory.getTariffDao()
					.deleteTariffById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
