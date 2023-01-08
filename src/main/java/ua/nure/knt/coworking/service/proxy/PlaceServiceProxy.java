package ua.nure.knt.coworking.service.proxy;

import ua.nure.knt.coworking.constants.RoleEnum;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.exceptions.AccessDenied;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.service.IPlaceService;

import java.util.List;

public class PlaceServiceProxy implements IPlaceService {
	private final IPlaceService placeService;

	public PlaceServiceProxy(IPlaceService placeService) {
		this.placeService = placeService;
	}

	@Override
	public Place readPlaceById(Integer id) {
		return placeService.readPlaceById(id);
	}

	@Override
	public List<Place> readAllPlaces() {
		return placeService.readAllPlaces();
	}

	@Override
	public List<Place> readAvailablePlaces(String rentPeriod) {
		return placeService.readAvailablePlaces(rentPeriod);
	}

	@Override
	public void savePlace(Place place, String role, ContentObserver contentObserver) {
		if (!RoleEnum.ROLE_ADMIN.name()
				.equals(role)) {
			throw new AccessDenied("Access denied. Create available only for admins");
		}
		placeService.savePlace(place, role, contentObserver);
	}

	@Override
	public void updatePlace(Place place, String role, ContentObserver contentObserver) {
		if (!RoleEnum.ROLE_ADMIN.name()
				.equals(role)) {
			throw new AccessDenied("Access denied. Create available only for admins");
		}
		placeService.updatePlace(place, role, contentObserver);
	}

	@Override
	public void deletePlace(Integer id, String role, ContentObserver contentObserver) {
		if (!RoleEnum.ROLE_ADMIN.name()
				.equals(role)) {
			throw new AccessDenied("Access denied. Create available only for admins");
		}
		placeService.deletePlace(id, role, contentObserver);
	}
}
