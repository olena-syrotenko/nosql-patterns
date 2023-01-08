package ua.nure.knt.coworking.controller;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.caretakers.TariffCaretaker;
import ua.nure.knt.coworking.dto.TariffDto;
import ua.nure.knt.coworking.entity.Tariff;
import ua.nure.knt.coworking.exceptions.AccessDenied;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.service.ITariffService;
import ua.nure.knt.coworking.service.ServiceFactory;
import ua.nure.knt.coworking.util.ConverterUtil;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class TariffController {
	private final ITariffService tariffService;

	public TariffController() {
		this.tariffService = ServiceFactory.getTariffService();
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
	String createTariff(@ModelAttribute TariffDto tariffForm, Model model, HttpSession session) {
		try {
			tariffService.saveTariff(ConverterUtil.toEntity(tariffForm), (String) session.getAttribute("userRole"), new ContentObserver(model));
		} catch (AccessDenied accessDenied) {
			model.addAttribute("error", accessDenied.getMessage());
			return "errorPage";
		}
		return "messagePage";
	}

	@PostMapping("/update-tariff/{id}")
	String updateTariff(@PathVariable Integer id, @ModelAttribute TariffDto tariffForm, Model model, HttpSession session) {
		Tariff tariff = ConverterUtil.toEntity(tariffForm);

		TariffCaretaker tariffCaretaker = ObjectUtils.defaultIfNull((TariffCaretaker) session.getAttribute("tariffCaretaker"), new TariffCaretaker());
		tariffCaretaker.saveState(((Tariff) session.getAttribute("tariffToUpdate")).saveState());
		session.setAttribute("tariffCaretaker", tariffCaretaker);

		try {
			tariffService.updateTariff(tariff, (String) session.getAttribute("userRole"), new ContentObserver(model));
		} catch (AccessDenied accessDenied) {
			model.addAttribute("error", accessDenied.getMessage());
			return "errorPage";
		}
		return "messagePage";
	}

	@PostMapping("/undo-update-tariff")
	String undoUpdateTariff(HttpSession session) {
		TariffCaretaker tariffCaretaker = (TariffCaretaker) session.getAttribute("tariffCaretaker");
		if (tariffCaretaker != null && !tariffCaretaker.isEmpty()) {
			Tariff tariff = new Tariff();
			tariff.restoreState(tariffCaretaker.restoreState());
			tariffService.updateTariff(tariff, (String) session.getAttribute("userRole"), null);
		}
		return "redirect:/tariffs";
	}

	@PostMapping("/delete-tariff/{id}")
	String deleteTariff(@PathVariable Integer id, Model model, HttpSession session) {
		try {
			tariffService.deleteTariff(id, (String) session.getAttribute("userRole"), new ContentObserver(model));
		} catch (AccessDenied accessDenied) {
			model.addAttribute("error", accessDenied.getMessage());
			return "errorPage";
		}
		return "messagePage";
	}
}
