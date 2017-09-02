/**
 * 
 */
package de.ceruti.curcuma.appkit.view.cells;

import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.appkit.view.NSViewBase.ViewPlugIn;

/**
 * Represents an abstract Widget. It may represent a Label, Field, Button and even a Cell used to render a table's cell.
 *
 */
public interface WidgetPlugin extends ViewPlugIn {
	
	/**
	 * returns the Association e.g. the cell using this plugin 
	 * @return
	 */
	Delegate getWidgetAssociation();

	/**
	 * @see #getWidgetAssociation()
	 */
	void setWidgetAssociation(Delegate d);

	/**
	 * the Widget of the underlying Widget-toolkit
	 * @return
	 */
	Object getWidget();

	/**
	 * Set the native Widget. If this cell is going to be used as a cell-renderer please to
	 * keep in mind that these should have good performance.
	 * Consider using native renderers (@see {@link #setNativeCellRenderer(Object)}
	 * @param w
	 */
	void setWidget(Object w);
	
	/**
	 * Provide a native Renderer. If it is present it will be used.
	 * @param nativeRenderer
	 */
	void setNativeCellRenderer(Object nativeRenderer);
	
	/**
	 * Provide a native Editor. If it is present it will be used.
	 * @param nativeEditor
	 */
	void setNativeCellEditor(Object nativeEditor);
	
	/**
	 * @see #setNativeCellEditor(Object)
	 * @return
	 */
	Object getNativeCellEditor();
	
	/**
	 * @see #setNativeCellRenderer(Object)
	 * @return
	 */
	Object getNativeCellRenderer();

	@Override
	boolean isVisible();

	@Override
	void setVisible(boolean b);

	// Responder
	@Override
	boolean isEnabled();

	@Override
	void setEnabled(boolean b);

	boolean isEditable();

	void setEditable(boolean b);

	void requestFocus();

	boolean hasFocus();

	/**
	 * Display Value
	 * 
	 * @return
	 */
	Object getPlugInValue();

	/**
	 * Sets the plugin's value.
	 * You must make sure that this does not trigger notifications such as the ones beeing triggered when
	 * editing occured from gui.
	 * 
	 * @param p
	 */
	void setPlugInValue(Object p);

	/**
	 * e.g. paint it red or beep. Maybe show message
	 * 
	 * @param m
	 */
	public void notifyMessage(String m);
	
	public interface Delegate {
		@Deprecated
		void setWidgetPlugIn(WidgetPlugin w);
		
		/**
		 * 
		 * @param sender
		 * @return TODO
		 */
		boolean plugInValueDidChangeViaGui(WidgetPlugin sender);

		public static class Dummy implements Delegate {
			@Override
			public void setWidgetPlugIn(WidgetPlugin w){
			}
			
			@Override
			public boolean plugInValueDidChangeViaGui(WidgetPlugin sender) {
				return true;
			}

			public static final Delegate INSTANCE = new Dummy();

		}
	}
	
	void setValidityStatus(boolean isValid, String reason);

	/**
	 * Note: Should always accept NSSelectionMarkers
	 * @param t
	 * @return
	 * @throws ValidationException
	 */
	public Object validatePlugInValue(Object t) throws ValidationException;
	
	
	Object getBackground();
	void setBackground(Object nativeColor);
	
	Object getForeground();
	void setForeground(Object nativeColor);
	
	
	class Dummy extends ViewPlugIn.Dummy implements WidgetPlugin {

		@Override
		public Object getBackground() {
			return null;
		}

		@Override
		public Object getNativeCellEditor() {
			return null;
		}

		@Override
		public Object getNativeCellRenderer() {
			return null;
		}

		@Override
		public Object getPlugInValue() {
			return null;
		}

		@Override
		public Object getWidget() {
			return null;
		}

		@Override
		public WidgetPlugin.Delegate getWidgetAssociation() {
			return null;
		}

		@Override
		public boolean hasFocus() {
			return false;
		}

		@Override
		public boolean isEditable() {
			return false;
		}

		@Override
		public boolean isEnabled() {
			return false;
		}

		@Override
		public boolean isVisible() {
			return false;
		}

		@Override
		public void notifyMessage(String m) {	
		}

		public boolean prefersNativeCellRenderer() {
			return false;
		}

		@Override
		public void requestFocus() {
		}

		@Override
		public void setBackground(Object nativeColor) {
		}

		@Override
		public void setEditable(boolean b) {	
		}

		@Override
		public void setEnabled(boolean b) {
		}

		@Override
		public void setNativeCellEditor(Object nativeEditor) {
		}

		@Override
		public void setNativeCellRenderer(Object nativeRenderer) {
		}

		@Override
		public void setPlugInValue(Object p) {	
		}

		public void setPrefersNativeCellRenderer(boolean b) {
		}

		@Override
		public void setVisible(boolean b) {
		}

		@Override
		public void setWidget(Object w) {
		}

		@Override
		public void setWidgetAssociation(WidgetPlugin.Delegate d) {
		}

		@Override
		public Object validatePlugInValue(Object t) throws ValidationException {
			return null;
		}
		
		public static WidgetPlugin INSTANCE = new WidgetPlugin.Dummy();

		@Override
		public Object getForeground() {
			return null;
		}

		@Override
		public void setForeground(Object nativeColor) {
		}

		@Override
		public void setValidityStatus(boolean isValid, String reason) {
		}
		
		@Override
		public boolean isDummy() {
			return true;
		}
	}

	boolean isDummy();
	
}