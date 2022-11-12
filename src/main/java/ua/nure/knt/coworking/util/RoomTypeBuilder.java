package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.RoomType;

public class RoomTypeBuilder implements Builder<RoomType> {
	private Integer id;
	private String name;
	private String description;
	private String image;

	public RoomTypeBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public RoomTypeBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public RoomTypeBuilder setDescription(String description) {
		this.description = description;
		return this;
	}

	public RoomTypeBuilder setImage(String image) {
		this.image = image;
		return this;
	}

	@Override
	public RoomType build() {
		RoomType roomType = new RoomType();
		roomType.setId(this.id);
		roomType.setName(this.name);
		roomType.setDescription(this.description);
		roomType.setImage(this.image);
		return roomType;
	}
}
