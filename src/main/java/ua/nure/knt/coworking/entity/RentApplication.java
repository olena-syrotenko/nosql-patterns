package ua.nure.knt.coworking.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class RentApplication {
	private Integer id;
	private LocalDateTime createDate;
	private LocalDateTime lastChange;
	private String leaseAgreement;
	private Status status;
	private User user;
	private Double rentAmount;
	private List<RentPlace> rentPlaces;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public LocalDateTime getLastChange() {
		return lastChange;
	}

	public void setLastChange(LocalDateTime lastChange) {
		this.lastChange = lastChange;
	}

	public String getLeaseAgreement() {
		return leaseAgreement;
	}

	public void setLeaseAgreement(String lease_agreement) {
		this.leaseAgreement = lease_agreement;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Double getRentAmount() {
		return rentAmount;
	}

	public void setRentAmount(Double rentAmount) {
		this.rentAmount = rentAmount;
	}

	public List<RentPlace> getRentPlaces() {
		return rentPlaces;
	}

	public void setRentPlaces(List<RentPlace> rentPlaces) {
		this.rentPlaces = rentPlaces;
	}

	@Override
	public String toString() {
		return "RentApplication{" + "id=" + id + ( createDate == null ? "" : ", createDate=" + createDate) +
				(lastChange == null ? "" : ", lastChange=" + lastChange) +
				(leaseAgreement == null ? "" : ", leaseAgreement='" + leaseAgreement) + '\''
				+ ", status=" + status.getName() +
				(user == null ? "" : ", user=" + user.getEmail()) +
				(rentAmount == null ? "" : ", rentAmount=" + rentAmount) +
				(rentPlaces == null ? "" : ", rentPlaces="
				+ rentPlaces.stream()
				.map(RentPlace::toString)
				.collect(Collectors.joining(", "))) + '}';
	}
}
