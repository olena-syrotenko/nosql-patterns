package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.observers.ContentObserver;

import java.util.List;

public interface ITariffService {
	List<Tariff> readAllTariffs();

	Tariff readTariffById(Integer id);

	void saveTariff(Tariff tariff, String role, ContentObserver contentObserver);

	void updateTariff(Tariff tariff, String role, ContentObserver contentObserver);

	void deleteTariff(Integer id, String role, ContentObserver contentObserver);
}
