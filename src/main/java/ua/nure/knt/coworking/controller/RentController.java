package ua.nure.knt.coworking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.constants.StatusEnum;
import ua.nure.knt.coworking.dto.RentApplicationDto;
import ua.nure.knt.coworking.entity.RentApplication;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.observers.LoggerObserver;
import ua.nure.knt.coworking.observers.ModificationObservable;
import ua.nure.knt.coworking.service.RentService;
import ua.nure.knt.coworking.util.ConverterUtil;

import java.util.List;

@Controller
public class RentController {
	private final RentService rentService;
	private final ModificationObservable rentObservable;
	private final ContentObserver contentObserver;

	@Autowired
	public RentController(RentService rentService) {
		this.rentService = rentService;
		this.rentObservable = new ModificationObservable();
		this.contentObserver = new ContentObserver();
		rentObservable.attach(new LoggerObserver());
		rentObservable.attach(contentObserver);
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
	String createApplication(@ModelAttribute RentApplicationDto rentApplicationForm, Model model) {
		rentApplicationForm.setStatus(StatusEnum.NEW.getStatus());
		RentApplication rentApplication = ConverterUtil.toEntity(rentApplicationForm);
		rentService.saveRentApplication(rentApplication);
		rentObservable.notify("New rent application " + rentApplication + " was created");
		model.addAttribute("infoMessage", contentObserver.getModel().getAttribute("content"));
		return "messagePage";
	}

	@PostMapping("/update-application")
	String updateApplication(@ModelAttribute RentApplicationDto rentApplicationForm, Model model) {
		RentApplication rentApplication = ConverterUtil.toEntity(rentApplicationForm);
		rentService.updateRentApplicationStatus(rentApplication);
		rentObservable.notify("Status of rent application was updated: " + rentApplication);
		model.addAttribute("infoMessage", contentObserver.getModel().getAttribute("content"));
		return "messagePage";
	}

}
