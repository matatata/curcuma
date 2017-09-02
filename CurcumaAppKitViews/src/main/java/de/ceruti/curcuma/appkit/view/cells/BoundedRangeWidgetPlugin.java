package de.ceruti.curcuma.appkit.view.cells;


import de.ceruti.curcuma.api.core.ValueTransformer;

public interface BoundedRangeWidgetPlugin extends ActionControlWidgetPlugin {

	int getMaximumPlugInValue();
	int getMinimumPlugInValue();
	void setMinimumPlugInValue(int v);
	void setMaximumPlugInValue(int v);
	
	void setValue(int v);
	int getValue();

	interface Delegate extends ActionControlWidgetPlugin.Delegate {
		
		ValueTransformer getBoundedRangeTransformer();
		
		class Dummy extends ActionControlWidgetPlugin.Delegate.Dummy implements Delegate {
			public static final Delegate INSTANCE = new Delegate.Dummy();

			@Override
			public ValueTransformer getBoundedRangeTransformer(){
				return ValueTransformer.Identity;
			}
			

		}
	}
}
