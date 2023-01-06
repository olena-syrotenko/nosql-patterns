package ua.nure.knt.coworking.observers;

import java.util.ArrayList;
import java.util.List;

public class ModificationObservable implements Observable {
	private final List<Observer> observers = new ArrayList<>();

	@Override
	public void attach(Observer observer) {
		observers.add(observer);
	}

	@Override
	public void detach(Observer observer) {
		observers.remove(observer);
	}

	@Override
	public void notify(String notificationMessage) {
		observers.forEach(observer -> observer.update(notificationMessage));
	}
}
