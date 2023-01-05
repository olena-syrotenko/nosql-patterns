package ua.nure.knt.coworking.dto;

import ua.nure.knt.coworking.entity.RoomType;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.TimeUnit;

import java.util.List;

public class TariffDto {
	private Integer id;
	private String name;
	private Double price;
	private TimeUnit timeUnit;
	private RoomType roomType;
	private String services;

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

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(TimeUnit timeUnit) {
		this.timeUnit = timeUnit;
	}

	public RoomType getRoomType() {
		return roomType;
	}

	public void setRoomType(RoomType roomType) {
		this.roomType = roomType;
	}

	public String getServices() {
		return services;
	}

	public void setServices(String services) {
		this.services = services;
	}
}
