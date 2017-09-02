package de.ceruti.curcuma.appkit.view.cells;


public interface EditorWidgetPlugin extends WidgetPlugin {

	interface Delegate extends WidgetPlugin.Delegate {
		void plugInDidBeginEditing(EditorWidgetPlugin sender);

		void plugInDidCancelEditing(EditorWidgetPlugin sender);

		class Dummy extends WidgetPlugin.Delegate.Dummy implements Delegate {
			@Override
			public void plugInDidBeginEditing(EditorWidgetPlugin sender) {
			}

			@Override
			public void plugInDidCancelEditing(EditorWidgetPlugin sender) {
			}

			public static final Delegate INSTANCE = new Delegate.Dummy();
		}
	}
}
