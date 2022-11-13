package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.RoomType;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.entity.TimeUnit;

import java.util.ArrayList;
import java.util.List;

public class TariffBuilder implements Builder<Tariff> {
	private Integer id;
	private String name;
	private Double price;
	private TimeUnit timeUnit;
	private RoomType roomType;
	private final List<Service> services;

	public TariffBuilder() {
		services = new ArrayList<>();
	}

	public TariffBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public TariffBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public TariffBuilder setPrice(Double price) {
		this.price = price;
		return this;
	}

	public TariffBuilder setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
		return this;
	}

	public TariffBuilder setRoomType(RoomType roomType) {
		this.roomType = roomType;
		return this;
	}

	public TariffBuilder setService(Service service) {
		this.services.add(service);
		return this;
	}

	@Override
	public Tariff build() {
		Tariff tariff = new Tariff();
		tariff.setId(this.id);
		tariff.setName(this.name);
		tariff.setPrice(this.price);
		tariff.setTimeUnit(this.timeUnit);
		tariff.setRoomType(this.roomType);
		tariff.setServices(this.services);
		return tariff;
	}
}
