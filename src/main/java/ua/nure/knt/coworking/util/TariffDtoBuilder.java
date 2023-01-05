package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.dto.TariffDto;
import ua.nure.knt.coworking.entity.RoomType;
import ua.nure.knt.coworking.entity.TimeUnit;

public class TariffDtoBuilder implements Builder<TariffDto> {
	private Integer id;
	private String name;
	private Double price;
	private TimeUnit timeUnit;
	private RoomType roomType;
	private String services;

	public TariffDtoBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public TariffDtoBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public TariffDtoBuilder setPrice(Double price) {
		this.price = price;
		return this;
	}

	public TariffDtoBuilder setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}

	public TariffDtoBuilder setRoomType(RoomType roomType) {
		this.roomType = roomType;
		return this;
	}

	public TariffDtoBuilder setServices(String services) {
		this.services = services;
		return this;
	}

	@Override
	public TariffDto build() {
		TariffDto tariffDto = new TariffDto();
		tariffDto.setId(this.id);
		tariffDto.setName(this.name);
		tariffDto.setPrice(this.price);
		tariffDto.setTimeUnit(this.timeUnit);
		tariffDto.setRoomType(this.roomType);
		tariffDto.setServices(this.services);
		return tariffDto;
	}
}
