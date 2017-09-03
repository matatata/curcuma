package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import de.ceruti.curcuma.appkit.view.cells.ToggleWidgetPlugin;

public abstract class AbstractToggleWidgetPlugIn extends AWTWidgetPlugIn
		implements ToggleWidgetPlugin {
	
	protected class DefaultItemListener implements ItemListener {
		/**
		 * simply calls selectionEditedViaGui of
		 * {@link AbstractToggleWidgetPlugIn}
		 */
		@Override
		public void itemStateChanged(ItemEvent e) {
			selectionEditedViaGui();
		}

	}

	protected ItemListener myItemListener;

	protected AbstractToggleWidgetPlugIn() {
		super();
	}

	@Override
	public void breakCellConnection() {
		removeItemListener(myItemListener);
		myItemListener = null;
		super.breakCellConnection();
	}

	@Override
	public void establishCellConnection() {
		super.establishCellConnection();
		myItemListener = createItemListener();
		addItemListener(myItemListener);
	}

	@Override
	public Object getPlugInValue() {
		return isSelected();
	}

	@Override
	protected void _setPlugInValue(Object p) {
		setSelected((Boolean) p);
	}

	protected abstract void addItemListener(ItemListener l);

	protected ItemListener createItemListener() {
		return new DefaultItemListener();
	}

	@Override
	public abstract boolean isSelected();

	protected abstract void removeItemListener(ItemListener l);
	
	protected void selectionEditedViaGui() {
		if (!notifyAssociationEnabled())
			return;
		getWidgetAssociation().plugInValueDidChangeViaGui(this);
		actionPerformed();
	}

	/**
	 * 
	 * @param s
	 */
	@Override
	public abstract void setSelected(boolean s);
	
	@Override
	public void setValidityStatus(boolean isValid, String reason) {
	}

}
