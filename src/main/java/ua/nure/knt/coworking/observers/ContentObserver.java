package ua.nure.knt.coworking.observers;

import org.springframework.ui.Model;

public class ContentObserver implements Observer {
	private final Model model;

	public ContentObserver(Model model) {
		this.model = model;
	}

	@Override
	public void update(String context) {
		model.addAttribute("content", context);
	}
}
