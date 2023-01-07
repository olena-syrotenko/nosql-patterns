package ua.nure.knt.coworking.caretakers;

import ua.nure.knt.coworking.util.TariffMemento;

import java.util.Stack;

public class TariffCaretaker {
	private final Stack<TariffMemento> tariffMementos = new Stack<>();

	public void saveState(TariffMemento tariffMemento) {
		if(tariffMemento != null) {
			tariffMementos.push(tariffMemento);
		}
	}

	public TariffMemento restoreState() {
		if(!tariffMementos.empty()) {
			return tariffMementos.pop();
		}
		return null;
	}

	public boolean isEmpty() {
		return tariffMementos.empty();
	}
}
