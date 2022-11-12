package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Room;
import ua.nure.knt.coworking.entity.RoomType;

public class RoomBuilder implements Builder<Room> {
	private Integer id;
	private String name;
	private Double area;
	private Integer maxPlaces;
	private RoomType roomType;

	public RoomBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public RoomBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public RoomBuilder setArea(Double area) {
		this.area = area;
		return this;
	}

	public RoomBuilder setMaxPlaces(Integer maxPlaces) {
		this.maxPlaces = maxPlaces;
		return this;
	}

	public RoomBuilder setRoomType(RoomType roomType) {
		this.roomType = roomType;
		return this;
	}

	@Override
	public Room build() {
		Room room = new Room();
		room.setId(this.id);
		room.setName(this.name);
		room.setArea(this.area);
		room.setMaxPlaces(this.maxPlaces);
		room.setRoomType(this.roomType);
		return room;
	}
}
