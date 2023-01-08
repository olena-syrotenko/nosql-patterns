package ua.nure.knt.coworking.service.proxy;

import ua.nure.knt.coworking.constants.RoleEnum;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.exceptions.AccessDenied;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.service.ITariffService;
import ua.nure.knt.coworking.service.implementations.TariffService;

import java.util.List;

public class TariffServiceProxy implements ITariffService {
	private final TariffService tariffService;

	public TariffServiceProxy() {
		this.tariffService = new TariffService();
	}

	@Override
	public List<Tariff> readAllTariffs() {
		return tariffService.readAllTariffs();
	}

	@Override
	public Tariff readTariffById(Integer id) {
		return tariffService.readTariffById(id);
	}

	@Override
	public void saveTariff(Tariff tariff, String role, ContentObserver contentObserver) {
		if (!RoleEnum.ROLE_ADMIN.name()
				.equals(role)) {
			throw new AccessDenied("Access denied. Create available only for admins");
		}
		tariffService.saveTariff(tariff, role, contentObserver);
	}

	@Override
	public void updateTariff(Tariff tariff, String role, ContentObserver contentObserver) {
		if (!RoleEnum.ROLE_ADMIN.name()
				.equals(role)) {
			throw new AccessDenied("Access denied. Update available only for admins");
		}
		tariffService.updateTariff(tariff, role, contentObserver);
	}

	@Override
	public void deleteTariff(Integer id, String role, ContentObserver contentObserver) {
		if (!RoleEnum.ROLE_ADMIN.name()
				.equals(role)) {
			throw new AccessDenied("Access denied. Delete available only for admins");
		}
		tariffService.deleteTariff(id, role, contentObserver);
	}
}
