package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.entity.Room;

public class PlaceBuilder implements Builder<Place> {
	private Integer id;
	private Double area;
	private Room room;

	public PlaceBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public PlaceBuilder setArea(Double area) {
		this.area = area;
		return this;
	}

	public PlaceBuilder setRoom(Room room) {
		this.room = room;
		return this;
	}

	@Override
	public Place build() {
		Place place = new Place();
		place.setId(this.id);
		place.setArea(this.area);
		place.setRoom(this.room);
		return place;
	}
}
