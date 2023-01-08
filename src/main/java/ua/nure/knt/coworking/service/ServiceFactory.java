package ua.nure.knt.coworking.service;

import ua.nure.knt.coworking.service.implementations.PlaceService;
import ua.nure.knt.coworking.service.implementations.RentService;
import ua.nure.knt.coworking.service.implementations.TariffService;
import ua.nure.knt.coworking.service.implementations.UserService;
import ua.nure.knt.coworking.service.proxy.PlaceServiceProxy;
import ua.nure.knt.coworking.service.proxy.TariffServiceProxy;

public class ServiceFactory {
	public static ITariffService getTariffService() {
		return new TariffServiceProxy(new TariffService());
	}

	public static IPlaceService getPlaceService() {
		return new PlaceServiceProxy(new PlaceService());
	}

	public static IRentService getRentService() {
		return new RentService();
	}

	public static IUserService getUserService() {
		return new UserService();
	}
}
