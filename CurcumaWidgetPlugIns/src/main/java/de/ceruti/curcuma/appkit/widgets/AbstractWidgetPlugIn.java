package de.ceruti.curcuma.appkit.widgets;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;

public abstract class AbstractWidgetPlugIn extends AbstractViewPlugIn implements
		WidgetPlugin {

	private static Category logger = Logger
			.getInstance(AbstractWidgetPlugIn.class);

	private WidgetPlugin.Delegate association;
	
	private Object nativeCellRenderer;
	private Object nativeCellEditor;
	private boolean notifyAssociationEnabled = true;

	protected AbstractWidgetPlugIn() {
	}

	public Object getCellEditorWidget(NSTableView table, Object value,
			boolean isSelected, int row, NSTableColumn col) {
		return getWidget();
	}

	public Object getTableCellRendererWidget(NSTableView table, Object value,
			boolean isSelected, boolean hasFocus, int row, NSTableColumn col) {

		return getWidget();
	}

	@Override
	public WidgetPlugin.Delegate getWidgetAssociation() {
		return association;
	}

	@Override
	public boolean isEditable() {
		return isEnabled();
	}

	@Override
	public abstract void notifyMessage(String m);

	@Override
	public void setEditable(boolean e) {
		setEnabled(e);
	}

	/**
	 * Sets the plugin's value. Before doing so notifying the widget association
	 * must be turned off via {@link #setNotifyAssociationEnabled(boolean)}
	 * respectively turned on afterwards.
	 * The editing-state must be turned off by using {@link #setDirty(boolean)}
	 * @param p
	 * @see de.ceruti.curcuma.appkit.view.cells.WidgetPlugin#setPlugInValue(Object)
	 * @see #setNotifyAssociationEnabled(boolean)
	 */
	@Override
	public final void setPlugInValue(Object p)
	{
		if(getWidget()==null)
		{
			logger.debug("setPlugInValue(): '" + p + "' no Widget present!");
			return;
		}
		setNotifyAssociationEnabled(false);
		logger.debug("setPlugInValue(): '" + p + "'");
		_setPlugInValue(p);
		setNotifyAssociationEnabled(true);
	}

	@Override
	public Object validatePlugInValue(Object o) throws ValidationException {
		return o;
	}
	
	protected abstract void _setPlugInValue(Object v);

	/*
	 * @see de.ceruti.curcuma.appkit.view.WidgetPlugin#getPlugInValue()
	 */
	@Override
	public abstract Object getPlugInValue();

	
	@Override
	public final void setWidgetAssociation(WidgetPlugin.Delegate association) {
		this.association = association;
	}

	protected void breakCellConnection() {
	}

	protected void establishCellConnection() {
	}
	
	@Override
	public final void setWidget(Object w) {
		
		if (getWidget() != w) {
			if (getWidget() != null)
				breakCellConnection();
			_setWidget(w);
			if(w != null)
				establishCellConnection();
		}
	}
	
	protected abstract void _setWidget(Object w);

	/**
	 * Ask whether input from GUI should triggers sending Notification to the
	 * Association
	 * 
	 * @return
	 */
	protected final boolean notifyAssociationEnabled() {
		return notifyAssociationEnabled;
	}

	/**
	 * Will and should only be called from {@link #setPlugInValue(Object)} before and after the plugin's value is changed.
	 * @param doNotify
	 * @see #setPlugInValue(Object)
	 */
	protected void setNotifyAssociationEnabled(boolean doNotify) {
		this.notifyAssociationEnabled = doNotify;
		logger.debug(this + ".setNotifyAssociationEnabled=" + doNotify);
	}

	@Override
	public final Object getNativeCellRenderer() {
		return nativeCellRenderer;
	}

	@Override
	public final void setNativeCellRenderer(Object nativeCellRenderer) {
		this.nativeCellRenderer = nativeCellRenderer;
	}

	@Override
	public final Object getNativeCellEditor() {
		return nativeCellEditor;
	}

	@Override
	public final void setNativeCellEditor(Object nativeCellEditor) {
		this.nativeCellEditor = nativeCellEditor;
	}
}
