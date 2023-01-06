package ua.nure.knt.coworking.observers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.nure.knt.coworking.NosqlPatternsApplication;

public class LoggerObserver implements Observer {
	private static final Logger logger = LoggerFactory.getLogger(NosqlPatternsApplication.class);

	@Override
	public void update(String context) {
		logger.info(context);
	}
}
