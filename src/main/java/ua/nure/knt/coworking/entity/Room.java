package ua.nure.knt.coworking.entity;

public class Room {
	private Integer id;
	private String name;
	private Double area;
	private Integer maxPlaces;
	private RoomType roomType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Integer getMaxPlaces() {
		return maxPlaces;
	}

	public void setMaxPlaces(Integer maxPlaces) {
		this.maxPlaces = maxPlaces;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	@Override
	public String toString() {
		return "Room{" + "id=" + id + ", name='" + name + '\'' + ", area=" + area + ", maxPlaces=" + maxPlaces + ", roomType=" + roomType.getName() + '}';
	}
}
