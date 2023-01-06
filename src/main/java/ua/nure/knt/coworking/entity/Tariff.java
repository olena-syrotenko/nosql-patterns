package ua.nure.knt.coworking.entity;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Tariff {
	private Integer id;
	private String name;
	private Double price;
	private TimeUnit timeUnit;
	private RoomType roomType;
	private List<Service> services;

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

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}

	@Override
	public String toString() {
		return "Tariff{ name='" + name + '\'' + ", price=" + price + (timeUnit == null ? "" : ", timeUnit=" + timeUnit.getName()) +
				(roomType == null ?	"" : ", roomType=" + roomType.getName()) + ", services=" + CollectionUtils.emptyIfNull(services)
				.stream()
				.map(Service::getName)
				.collect(Collectors.joining(", ")) + '}';
	}
}
