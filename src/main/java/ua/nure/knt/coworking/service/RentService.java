package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.entity.RentApplication;

import java.sql.SQLException;
import java.util.List;

public class RentService {
	private final RentDao rentDao;

	public RentService(DaoFactory daoFactory) throws SQLException {
		rentDao = DaoFactory.getDaoFactory(DaoType.MySQL)
				.getRentDao();
	}

	List<RentApplication> readAllApplications() throws SQLException {
		return rentDao.readAllRentApplication();
	}

	List<RentApplication> readAllUserApplications(String email) throws SQLException {
		return rentDao.readAllRentApplicationByUser(email);
	}

	void saveRentApplication(RentApplication rentApplication) throws SQLException {
		rentDao.createRentApplication(rentApplication);
	}

	void updateRentApplicationStatus(RentApplication rentApplication) throws SQLException {
		rentDao.updateRentApplicationStatus(rentApplication);
	}
}
