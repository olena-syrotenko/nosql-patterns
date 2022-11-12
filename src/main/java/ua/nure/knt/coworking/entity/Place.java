package ua.nure.knt.coworking.entity;

public class Place {
	private Integer id;
	private Double area;
	private Room room;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Double getArea() {
		return area;
	}

	public void setArea(Double area) {
		this.area = area;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "Place{" + "id=" + id + ", area=" + area + ", room=№" + room.getId() + ' ' + room.getName() + ", " + room.getRoomType()
				.getName() + '}';
	}
}
