package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.TimeUnit;

public class TimeUnitBuilder implements Builder<TimeUnit> {
	private Integer id;
	private String name;

	public TimeUnitBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public TimeUnitBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public TimeUnit build() {
		TimeUnit timeUnit = new TimeUnit();
		timeUnit.setId(this.id);
		timeUnit.setName(this.name);
		return timeUnit;
	}
}
