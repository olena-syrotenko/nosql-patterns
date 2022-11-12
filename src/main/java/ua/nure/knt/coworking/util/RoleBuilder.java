package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Role;

public class RoleBuilder implements Builder<Role> {
	private Integer id;
	private String name;

	public RoleBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public RoleBuilder setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public Role build() {
		Role role = new Role();
		role.setId(this.id);
		role.setName(this.name);
		return role;
	}
}
