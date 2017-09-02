package de.ceruti.curcuma.api.appkit.view;

import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;


public interface NSView extends KeyValueBindingCreation, NSResponder {
	boolean isVisible();
	void setVisible(boolean b);
}