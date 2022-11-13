package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Service;

public class ServiceBuilder implements Builder<Service> {
	private Integer id;
	private String name;

	public ServiceBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public ServiceBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public Service build() {
		Service service = new Service();
		service.setId(this.id);
		service.setName(this.name);
		return service;
	}
}
