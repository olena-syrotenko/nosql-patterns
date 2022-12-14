package ua.nure.knt.coworking.service.implementations;

import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.TariffDao;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.observers.LoggerObserver;
import ua.nure.knt.coworking.service.ITariffService;

import java.sql.SQLException;
import java.util.List;

@Service
public class TariffService implements ITariffService {
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

	public void saveTariff(Tariff tariff, String role, ContentObserver contentObserver) {
		try {
			TariffDao tariffDao = daoFactory.getTariffDao();
			tariffDao.attach(new LoggerObserver());
			tariffDao.attach(contentObserver);
			tariffDao.createTariff(tariff);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateTariff(Tariff tariff, String role, ContentObserver contentObserver) {
		try {
			TariffDao tariffDao = daoFactory.getTariffDao();
			tariffDao.attach(new LoggerObserver());
			tariffDao.attach(contentObserver);
			tariffDao.updateTariff(tariff);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteTariff(Integer id, String role, ContentObserver contentObserver) {
		try {
			TariffDao tariffDao = daoFactory.getTariffDao();
			tariffDao.attach(new LoggerObserver());
			tariffDao.attach(contentObserver);
			tariffDao.deleteTariffById(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
