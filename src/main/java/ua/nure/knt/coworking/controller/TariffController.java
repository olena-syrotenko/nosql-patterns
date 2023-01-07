package ua.nure.knt.coworking.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.caretakers.TariffCaretaker;
import ua.nure.knt.coworking.dto.TariffDto;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.service.TariffService;
import ua.nure.knt.coworking.util.ConverterUtil;

import javax.servlet.http.HttpSession;
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
	String getTariffById(@PathVariable Integer id, Model model, HttpSession session) {
		Tariff tariffForm = tariffService.readTariffById(id);
		session.setAttribute("tariffToUpdate", tariffForm);
		model.addAttribute("tariffForm", ConverterUtil.toDto(tariffForm));
		return "tariffUpdatePage";
	}

	@PostMapping("/create-tariff")
	String createTariff(@ModelAttribute TariffDto tariffForm, Model model) {
		tariffService.saveTariff(ConverterUtil.toEntity(tariffForm), new ContentObserver(model));
		return "messagePage";
	}

	@PostMapping("/update-tariff/{id}")
	String updateTariff(@PathVariable Integer id, @ModelAttribute TariffDto tariffForm, Model model, HttpSession session) {
		Tariff tariff = ConverterUtil.toEntity(tariffForm);

		TariffCaretaker tariffCaretaker = ObjectUtils.defaultIfNull((TariffCaretaker) session.getAttribute("tariffCaretaker"), new TariffCaretaker());
		tariffCaretaker.saveState(((Tariff) session.getAttribute("tariffToUpdate")).saveState());
		session.setAttribute("tariffCaretaker", tariffCaretaker);

		tariffService.updateTariff(tariff, new ContentObserver(model));
		return "messagePage";
	}

	@PostMapping("/undo-update-tariff")
	String undoUpdateTariff(HttpSession session) {
		TariffCaretaker tariffCaretaker = (TariffCaretaker) session.getAttribute("tariffCaretaker");
		if (tariffCaretaker != null && !tariffCaretaker.isEmpty()) {
			Tariff tariff = new Tariff();
			tariff.restoreState(tariffCaretaker.restoreState());
			tariffService.updateTariff(tariff, null);
		}
		return "redirect:/tariffs";
	}

	@PostMapping("/delete-tariff/{id}")
	String deleteTariff(@PathVariable Integer id, Model model) {
		tariffService.deleteTariff(id, new ContentObserver(model));
		return "messagePage";
	}
}
