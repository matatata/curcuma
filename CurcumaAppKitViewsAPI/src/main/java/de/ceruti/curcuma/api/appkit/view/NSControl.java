package de.ceruti.curcuma.api.appkit.view;

import de.ceruti.curcuma.api.appkit.NSDefaultPlaceholders;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;

public interface NSControl extends
		NSView, KeyValueCoding, KeyValueObserving,
		KeyValueBindingCreation, NSDefaultPlaceholders {
	
	String ControlValueBinding = "controlValue";
	String EditableBinding = "editable";
	
	
	boolean isEditable();

	void setEditable(boolean b);

	void setCell(NSCell cell);

	NSCell getCell();

	Object getControlValue();

//	void updateControlValue() throws VerificationException;
	
	void setControlValue(Object obj);
	

	
	Object validateControlValue(Object ctrlVal) throws ValidationException;
}
