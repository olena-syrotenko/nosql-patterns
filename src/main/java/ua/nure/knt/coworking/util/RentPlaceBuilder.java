package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.entity.RentPlace;
import ua.nure.knt.coworking.entity.Tariff;

import java.time.LocalDate;

public class RentPlaceBuilder implements Builder<RentPlace> {
	private Place place;
	private LocalDate rentStart;
	private LocalDate rentEnd;
	private Double rentAmount;
	private Tariff tariff;

	public RentPlaceBuilder setPlace(Place place) {
		this.place = place;
		return this;
	}

	public RentPlaceBuilder setRentStart(LocalDate rentStart) {
		this.rentStart = rentStart;
		return this;
	}

	public RentPlaceBuilder setRentEnd(LocalDate rentEnd) {
		this.rentEnd = rentEnd;
		return this;
	}

	public RentPlaceBuilder setRentAmount(Double rentAmount) {
		this.rentAmount = rentAmount;
		return this;
	}

	public RentPlaceBuilder setTariff(Tariff tariff) {
		this.tariff = tariff;
		return this;
	}

	@Override
	public RentPlace build() {
		RentPlace rentPlace = new RentPlace();
		rentPlace.setPlace(this.place);
		rentPlace.setRentStart(this.rentStart);
		rentPlace.setRentEnd(this.rentEnd);
		rentPlace.setRentAmount(this.rentAmount);
		rentPlace.setTariff(this.tariff);
		return rentPlace;
	}
}
