package ua.nure.knt.coworking.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.nure.knt.coworking.entity.Place;
import ua.nure.knt.coworking.observers.ContentObserver;
import ua.nure.knt.coworking.observers.LoggerObserver;
import ua.nure.knt.coworking.observers.ModificationObservable;
import ua.nure.knt.coworking.service.PlaceService;

import java.util.List;

@Controller
public class PlaceController {
	private final PlaceService placeService;
	private final ModificationObservable placeObservable;
	private final ContentObserver contentObserver;

	@Autowired
	public PlaceController(PlaceService placeService) {
		this.placeService = placeService;
		this.placeObservable = new ModificationObservable();
		this.contentObserver = new ContentObserver();
		placeObservable.attach(new LoggerObserver());
		placeObservable.attach(contentObserver);
	}

	@GetMapping("/places")
	String getPlaces(Model model, @Param("rentInfo") String rentInfo) {
		List<Place> places = StringUtils.isBlank(rentInfo) ? placeService.readAllPlaces() : placeService.readAvailablePlaces(rentInfo);
		model.addAttribute("places", places);
		model.addAttribute("rentInfo", rentInfo);
		return "placesPage";
	}

	@GetMapping("/create-place")
	String getPlaceForm(Model model) {
		Place placeForm = new Place();
		model.addAttribute("placeForm", placeForm);
		return "placeCreatePage";
	}

	@GetMapping("/update-place/{id}")
	String getPlaceById(@PathVariable Integer id, Model model) {
		Place placeForm = placeService.readPlaceById(id);
		model.addAttribute("placeForm", placeForm);
		return "placeUpdatePage";
	}

	@PostMapping("/create-place")
	String createPlace(@ModelAttribute Place placeForm, Model model) {
		placeService.savePlace(placeForm);
		placeObservable.notify("New place " + placeForm + " was added");
		model.addAttribute("infoMessage", contentObserver.getModel().getAttribute("content"));
		return "messagePage";
	}

	@PostMapping("/update-place/{id}")
	String updatePlace(@PathVariable Integer id, @ModelAttribute Place placeForm, Model model) {
		placeService.updatePlace(placeForm);
		placeObservable.notify("Place " + id + " was updated: " + placeForm);
		model.addAttribute("infoMessage", contentObserver.getModel().getAttribute("content"));
		return "messagePage";
	}

	@PostMapping("/delete-place/{id}")
	String deletePlace(@PathVariable Integer id, Model model) {
		placeService.deletePlace(id);
		placeObservable.notify("Place " + id + " was deleted");
		model.addAttribute("infoMessage", contentObserver.getModel().getAttribute("content"));
		return "redirect:/places";
	}
}
