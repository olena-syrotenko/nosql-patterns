package ua.nure.knt.coworking.util;

import org.apache.commons.lang3.math.NumberUtils;
import ua.nure.knt.coworking.dto.RentApplicationDto;
import ua.nure.knt.coworking.dto.TariffDto;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.entity.RentPlace;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.Tariff;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ConverterUtil {
	public static Tariff toEntity(TariffDto tariffDto) {
		Tariff tariff = new TariffBuilder().setId(tariffDto.getId())
				.setName(tariffDto.getName())
				.setTimeUnit(tariffDto.getTimeUnit())
				.setRoomType(tariffDto.getRoomType())
				.setPrice(tariffDto.getPrice())
				.build();
		tariff.setServices(Arrays.stream(tariffDto.getServices()
						.split(";"))
				.map(service -> new ServiceBuilder().setName(service)
						.build())
				.collect(Collectors.toList()));
		return tariff;
	}

	public static TariffDto toDto(Tariff tariff) {
		return new TariffDtoBuilder().setId(tariff.getId())
				.setName(tariff.getName())
				.setTimeUnit(tariff.getTimeUnit())
				.setRoomType(tariff.getRoomType())
				.setPrice(tariff.getPrice())
				.setServices(tariff.getServices()
						.stream()
						.map(Service::getName)
						.collect(Collectors.joining(";")))
				.build();
	}

	public static RentApplication toEntity(RentApplicationDto rentApplicationDto) {
		RentApplication rentApplication = new RentApplicationBuilder().setId(rentApplicationDto.getId())
				.setStatus(new StatusBuilder().setName(rentApplicationDto.getStatus())
						.build())
				.setLeaseAgreement(rentApplicationDto.getLeaseAgreement())
				.setUser(new UserBuilder().setEmail(rentApplicationDto.getUser())
						.build())
				.build();

		if (rentApplicationDto.getRentPlaces() != null) {
			ArrayList<RentPlace> rentPlaces = new ArrayList<>();
			String[] rentPlacesString = rentApplicationDto.getRentPlaces()
					.split(";");
			for (String rentPlace : rentPlacesString) {
				String[] rentPlaceInfo = rentPlace.split(",");
				rentPlaces.add(new RentPlaceBuilder().setPlace(new PlaceBuilder().setId(NumberUtils.toInt(rentPlaceInfo[0]))
								.build())
						.setRentStart(LocalDate.parse(rentPlaceInfo[1]))
						.setRentEnd(LocalDate.parse(rentPlaceInfo[2]))
						.setTariff(new TariffBuilder().setName(rentPlaceInfo[3])
								.build())
						.build());
			}
			rentApplication.setRentPlaces(rentPlaces);
		}
		return rentApplication;
	}
}
