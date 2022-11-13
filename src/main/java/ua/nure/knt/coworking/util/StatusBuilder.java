package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Status;

public class StatusBuilder implements Builder<Status> {
	private Integer id;
	private String name;

	public StatusBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public StatusBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public Status build() {
		Status status = new Status();
		status.setId(this.id);
		status.setName(this.name);
		return status;
	}
}
