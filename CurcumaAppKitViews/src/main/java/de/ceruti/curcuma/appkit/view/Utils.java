package de.ceruti.curcuma.appkit.view;

import de.ceruti.curcuma.api.appkit.NSDefaultPlaceholders;
import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluebinding.NoSuchBindingOption;
import de.ceruti.curcuma.keyvaluebinding.BindingUtils;

public final class Utils {
	public static Object getPlaceholderForMarker(KeyValueBindingCreation kvb, NSDefaultPlaceholders defaultPlaceholders, String binding, NSSelectionMarker marker) {

		BindingInfo info = kvb.getInfoForBinding(binding);
		if (info == null)
			return marker;
		
		BindingOptions opts = info.getOptions();

		Object ret;
		try {
			ret = BindingUtils.getCustomBindingOptionValue(kvb, binding,
					marker.getBindingOptionKey(), opts);
			return ret;
		} catch (NoSuchBindingOption e) {
			Object placeholder = defaultPlaceholders
					.getDefaultPlaceholderForMarkerWithBinding(marker, binding);
			return placeholder;
		}
	}
	
	private Utils(){}
}
