package de.ceruti.curcuma.api.keyvaluebinding;

public class NoSuchBindingOption extends Exception {

	public NoSuchBindingOption(String binding, String optionKey) {
		super("No such Binding-Option " + optionKey + " for binding=" + binding);
	}

}
