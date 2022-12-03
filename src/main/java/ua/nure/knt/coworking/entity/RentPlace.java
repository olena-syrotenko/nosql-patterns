package ua.nure.knt.coworking.entity;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;

public class RentPlace {
	private Place place;
	private LocalDate rentStart;
	private LocalDate rentEnd;
	private Double rentAmount;
	private Tariff tariff;

	public Place getPlace() {
		return place;
	}

	public void setPlace(Place place) {
		this.place = place;
	}

	public LocalDate getRentStart() {
		return rentStart;
	}

	public void setRentStart(LocalDate rentStart) {
		this.rentStart = rentStart;
	}

	public LocalDate getRentEnd() {
		return rentEnd;
	}

	public void setRentEnd(LocalDate rentEnd) {
		this.rentEnd = rentEnd;
	}

	public Double getRentAmount() {
		return rentAmount;
	}

	public void setRentAmount(Double rentAmount) {
		this.rentAmount = rentAmount;
	}

	public Tariff getTariff() {
		return tariff;
	}

	public void setTariff(Tariff tariff) {
		this.tariff = tariff;
	}

	@Override
	public String toString() {
		return "RentPlace{" + "place=№" + place.getId() + " in room №" + place.getRoom()
				.getId() + " " + StringUtils.defaultIfBlank(place.getRoom()
				.getName(), "") + ", rentStart=" + rentStart + ", rentEnd=" + rentEnd + ", rentAmount=" + rentAmount + ", tariff=" + tariff.getName() + '}';
	}
}
