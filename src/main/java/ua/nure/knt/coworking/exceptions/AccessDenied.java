package ua.nure.knt.coworking.exceptions;

public class AccessDenied extends RuntimeException {
	public AccessDenied(String message) {
		super(message);
	}
}
