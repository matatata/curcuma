package de.ceruti.curcuma.appkit;

import de.ceruti.curcuma.api.appkit.NSDefaultPlaceholders;
import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.core.utils.MapMap;

public class NSPlaceholdersSupport implements NSDefaultPlaceholders {

	private MapMap<String, String, Object> theMap = new MapMap<String, String, Object>();

	public NSPlaceholdersSupport() {
	}
	
	@Override
	public Object getDefaultPlaceholderForMarkerWithBinding(NSSelectionMarker marker,
			String binding) {
		if(!theMap.containsKey(binding) || !theMap.get(binding).containsKey(marker.getBindingOptionKey()))
			return marker;
		return theMap.get(binding).get(marker.getBindingOptionKey());
	}

	@Override
	public void setDefaultPlaceholderForMarkerWithBinding(Object placeholder,
	    NSSelectionMarker marker, String binding) {
		theMap.put(binding, marker.getBindingOptionKey() ,placeholder);
	}
}
