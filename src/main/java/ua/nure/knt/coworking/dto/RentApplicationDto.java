package ua.nure.knt.coworking.dto;

public class RentApplicationDto {
	private Integer id;
	private String leaseAgreement;
	private String status;
	private String user;
	private String rentPlaces;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLeaseAgreement() {
		return leaseAgreement;
	}

	public void setLeaseAgreement(String lease_agreement) {
		this.leaseAgreement = lease_agreement;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRentPlaces() {
		return rentPlaces;
	}

	public void setRentPlaces(String rentPlaces) {
		this.rentPlaces = rentPlaces;
	}
}
