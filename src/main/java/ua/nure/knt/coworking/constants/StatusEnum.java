package ua.nure.knt.coworking.constants;

public enum StatusEnum {

	NEW("New"), CONFIRMED("Confirmed"), REJECTED("Rejected"), CANCELLED("Cancelled"), PAID("Paid");

	final String statusName;

	StatusEnum(String status) {
		statusName = status;
	}

	public String getStatus() {
		return statusName;
	}
}
