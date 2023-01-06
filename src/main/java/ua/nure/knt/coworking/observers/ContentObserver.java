package ua.nure.knt.coworking.observers;

import org.springframework.ui.Model;
import org.springframework.validation.support.BindingAwareModelMap;

public class ContentObserver implements Observer {
	private final Model model = new BindingAwareModelMap();

	public Model getModel() {
		return model;
	}

	@Override
	public void update(String context) {
		model.addAttribute("content", context);
	}
}
