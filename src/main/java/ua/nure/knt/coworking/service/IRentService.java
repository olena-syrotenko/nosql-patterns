package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.observers.ContentObserver;

import java.util.List;

public interface IRentService {
	List<RentApplication> readAllApplications();

	List<RentApplication> readAllUserApplications(String email);

	void saveRentApplication(RentApplication rentApplication, ContentObserver contentObserver);

	void updateRentApplicationStatus(RentApplication rentApplication, ContentObserver contentObserver);
}
