package ua.nure.knt.coworking.service;

import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.util.StatusBuilder;

import java.sql.SQLException;
import java.util.List;

@Service
public class RentService {
	private final DaoFactory daoFactory;

	public RentService() throws SQLException {
		daoFactory = DaoFactory.getDaoFactory(DaoType.MySQL);
	}

	public List<RentApplication> readAllApplications() {
		try {
			return daoFactory.getRentDao()
					.readAllRentApplication();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<RentApplication> readAllUserApplications(String email) {
		try {
			return daoFactory.getRentDao()
					.readAllRentApplicationByUser(email);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveRentApplication(RentApplication rentApplication) {
		try {
			rentApplication.setStatus(new StatusBuilder().setName(StatusEnum.NEW.getStatus())
					.build());
			daoFactory.getRentDao()
					.createRentApplication(rentApplication);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRentApplicationStatus(RentApplication rentApplication) {
		try {
			daoFactory.getRentDao()
					.updateRentApplicationStatus(rentApplication);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
