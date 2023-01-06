package ua.nure.knt.coworking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dto.RentApplicationDto;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.service.RentService;
import ua.nure.knt.coworking.util.ConverterUtil;

import java.util.List;

@Controller
public class RentController {
	private final RentService rentService;

	@Autowired
	public RentController(RentService rentService) {
		this.rentService = rentService;
	}

	@GetMapping("/applications")
	String getApplications(Model model) {
		List<RentApplication> rentApplications = rentService.readAllApplications();
		model.addAttribute("rentApplications", rentApplications);
		return "applicationsPage";
	}

	@GetMapping("/create-application")
	String getApplicationForm(Model model) {
		model.addAttribute("rentApplicationForm", new RentApplicationDto());
		return "applicationCreatePage";
	}

	@PostMapping("/create-application")
	String createApplication(@ModelAttribute RentApplicationDto rentApplicationForm) {
		rentApplicationForm.setStatus(StatusEnum.NEW.getStatus());
		rentService.saveRentApplication(ConverterUtil.toEntity(rentApplicationForm));
		return "redirect:/applications";
	}

	@PostMapping("/update-application")
	String updateApplication(@ModelAttribute RentApplicationDto rentApplicationForm) {
		rentService.updateRentApplicationStatus(ConverterUtil.toEntity(rentApplicationForm));
		return "redirect:/applications";
	}

}
