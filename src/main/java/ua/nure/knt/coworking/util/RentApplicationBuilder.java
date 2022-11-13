package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.entity.RentPlace;
import ua.nure.knt.coworking.entity.Status;
import ua.nure.knt.coworking.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RentApplicationBuilder implements Builder<RentApplication> {
	private Integer id;
	private LocalDateTime createDate;
	private LocalDateTime lastChange;
	private String leaseAgreement;
	private Status status;
	private User user;
	private Double rentAmount;
	private final List<RentPlace> rentPlaces;

	public RentApplicationBuilder() {
		rentPlaces = new ArrayList<>();
	}

	public RentApplicationBuilder setId(Integer id) {
		this.id = id;
		return this;
	}

	public RentApplicationBuilder setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
		return this;
	}

	public RentApplicationBuilder setLastChange(LocalDateTime lastChange) {
		this.lastChange = lastChange;
		return this;
	}

	public RentApplicationBuilder setLeaseAgreement(String leaseAgreement) {
		this.leaseAgreement = leaseAgreement;
		return this;
	}

	public RentApplicationBuilder setStatus(Status status) {
		this.status = status;
		return this;
	}

	public RentApplicationBuilder setUser(User user) {
		this.user = user;
		return this;
	}

	public RentApplicationBuilder setRentAmount(Double rentAmount) {
		this.rentAmount = rentAmount;
		return this;
	}

	public RentApplicationBuilder setRentPlace(RentPlace rentPlace) {
		this.rentPlaces.add(rentPlace);
		return this;
	}

	@Override
	public RentApplication build() {
		RentApplication rentApplication = new RentApplication();
		rentApplication.setId(this.id);
		rentApplication.setCreateDate(this.createDate);
		rentApplication.setLastChange(this.lastChange);
		rentApplication.setLeaseAgreement(this.leaseAgreement);
		rentApplication.setStatus(this.status);
		rentApplication.setUser(this.user);
		rentApplication.setRentAmount(this.rentAmount);
		rentApplication.setRentPlaces(this.rentPlaces);
		return rentApplication;
	}
}
