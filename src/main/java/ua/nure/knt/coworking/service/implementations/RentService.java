package ua.nure.knt.coworking.service.implementations;

import org.springframework.stereotype.Service;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dao.DaoFactory;
import ua.nure.knt.coworking.dao.DaoType;
import ua.nure.knt.coworking.dao.RentDao;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.observers.LoggerObserver;
import ua.nure.knt.coworking.service.IRentService;
import ua.nure.knt.coworking.util.StatusBuilder;

import java.sql.SQLException;
import java.util.List;

@Service
public class RentService implements IRentService {
	private final DaoFactory daoFactory;

	public RentService() {
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

	public void saveRentApplication(RentApplication rentApplication, ContentObserver contentObserver) {
		try {
			rentApplication.setStatus(new StatusBuilder().setName(StatusEnum.NEW.getStatus())
					.build());
			RentDao rentDao = daoFactory.getRentDao();
			rentDao.attach(new LoggerObserver());
			rentDao.attach(contentObserver);
			rentDao.createRentApplication(rentApplication);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateRentApplicationStatus(RentApplication rentApplication, ContentObserver contentObserver) {
		try {
			RentDao rentDao = daoFactory.getRentDao();
			rentDao.attach(new LoggerObserver());
			rentDao.attach(contentObserver);
			rentDao.updateRentApplicationStatus(rentApplication);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
