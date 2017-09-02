package de.ceruti.curcuma.appkit.view.cells;




public interface ActionControlWidgetPlugin extends WidgetPlugin {

	
	interface Delegate extends WidgetPlugin.Delegate {
		/**
		 * A plugin might optionally ask before performing the action
		 * @param sender
		 * @return
		 */
		boolean pluginShouldPerformAction(ActionControlWidgetPlugin sender);
		void pluginDidPerformAction(ActionControlWidgetPlugin sender);
		
		static class Dummy extends WidgetPlugin.Delegate.Dummy implements Delegate {
			
			@Override
			public void pluginDidPerformAction(ActionControlWidgetPlugin sender) {
			}

			@Override
			public boolean pluginShouldPerformAction(
					ActionControlWidgetPlugin sender) {
				return true;
			}

			public static final Delegate INSTANCE = new ActionControlWidgetPlugin.Delegate.Dummy();
		}
	}
}