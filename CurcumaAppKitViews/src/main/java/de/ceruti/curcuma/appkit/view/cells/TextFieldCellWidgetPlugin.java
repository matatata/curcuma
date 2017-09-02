package de.ceruti.curcuma.appkit.view.cells;

import de.ceruti.curcuma.api.appkit.view.NSText;

public interface TextFieldCellWidgetPlugin extends EditorWidgetPlugin,
		ActionControlWidgetPlugin, NSText {

	static interface Delegate extends EditorWidgetPlugin.Delegate,
			NSText.Delegate, ActionControlWidgetPlugin.Delegate {


		
		Object getEditBackground();
		Object getInvalidForeground();
		void setInvalidForeground(Object nativeColor);
		void setEditBackground(Object nativeColor);
		
				
		public static class Dummy extends NSText.Delegate.Dummy implements
				Delegate {
			
			@Override
			public void setWidgetPlugIn(WidgetPlugin w) {
			}
			
			@Override
			public boolean plugInValueDidChangeViaGui(WidgetPlugin sender) {
				return true;
			}

			@Override
			public void plugInDidCancelEditing(EditorWidgetPlugin sender) {
			}

			@Override
			public void plugInDidBeginEditing(EditorWidgetPlugin sender) {
			}

			@Override
			public void pluginDidPerformAction(ActionControlWidgetPlugin sender) {
			}

			@Override
			public boolean pluginShouldPerformAction(
					ActionControlWidgetPlugin sender) {
				return true;
			}

			@Override
			public Object getEditBackground() {
				return null;
			}
			@Override
			public Object getInvalidForeground() {
				return null;
			}
			@Override
			public void setEditBackground(Object nativeColor) {
			}

			@Override
			public void setInvalidForeground(Object nativeColor) {
			}
			
			public static final Delegate INSTANCE = new Delegate.Dummy();

			


			
		}
	}

}