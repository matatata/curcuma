package de.ceruti.curcuma.api.appkit;


public interface NSDefaultPlaceholders {
	void setDefaultPlaceholderForMarkerWithBinding(Object placeholder,
			NSSelectionMarker marker, String binding);

	Object getDefaultPlaceholderForMarkerWithBinding(NSSelectionMarker marker,
			String binding);
}
