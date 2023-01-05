package ua.nure.knt.coworking.util;

import ua.nure.knt.coworking.dto.TariffDto;
import ua.nure.knt.coworking.entity.Service;
import ua.nure.knt.coworking.entity.Tariff;

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
}
