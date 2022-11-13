package ua.nure.knt.coworking.entity;

public class Service {
	private Integer id;
	private String name;

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

	@Override
	public String toString() {
		return "Service{" + "id=" + id + ", name='" + name + '\'' + '}';
	}
}
