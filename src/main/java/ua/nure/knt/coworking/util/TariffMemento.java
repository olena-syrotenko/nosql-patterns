package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.entity.Tariff;

public class TariffMemento {
	private final Tariff tariff;

	public TariffMemento(Tariff tariff) {
		this.tariff = tariff;
	}

	public Tariff getState() {
		return tariff;
	}
}
