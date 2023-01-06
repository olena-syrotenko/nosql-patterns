package ua.nure.knt.coworking.observers;

public interface Observable {
	void attach(Observer observer);

	void detach(Observer observer);

	void notify(String notificationMessage);
}
