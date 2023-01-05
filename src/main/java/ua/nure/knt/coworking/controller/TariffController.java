package ua.nure.knt.coworking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.dto.TariffDto;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.service.TariffService;
import ua.nure.knt.coworking.util.ConverterUtil;

import java.util.List;

@Controller
public class TariffController {
	private final TariffService tariffService;

	@Autowired
	public TariffController(TariffService tariffService) {
		this.tariffService = tariffService;
	}

	@GetMapping("/tariffs")
	String getTariffs(Model model) {
		List<Tariff> tariffs = tariffService.readAllTariffs();
		model.addAttribute("tariffs", tariffs);
		return "tariffsPage";
	}

	@GetMapping("/create-tariff")
	String getTariffForm(Model model) {
		model.addAttribute("tariffForm", new TariffDto());
		return "tariffCreatePage";
	}

	@GetMapping("/update-tariff/{id}")
	String getTariffById(@PathVariable Integer id, Model model) {
		Tariff tariffForm = tariffService.readTariffById(id);
		model.addAttribute("tariffForm", ConverterUtil.toDto(tariffForm));
		return "tariffUpdatePage";
	}

	@PostMapping("/create-tariff")
	String createTariff(@ModelAttribute TariffDto tariffForm) {
		tariffService.saveTariff(ConverterUtil.toEntity(tariffForm));
		return "redirect:/tariffs";
	}

	@PostMapping("/update-tariff/{id}")
	String updateTariff(@PathVariable Integer id, @ModelAttribute TariffDto tariffForm) {
		tariffService.updateTariff(ConverterUtil.toEntity(tariffForm));
		return "redirect:/tariffs";
	}

	@PostMapping("/delete-tariff/{id}")
	String deleteTariff(@PathVariable Integer id) {
		tariffService.deleteTariff(id);
		return "redirect:/tariffs";
	}
}
