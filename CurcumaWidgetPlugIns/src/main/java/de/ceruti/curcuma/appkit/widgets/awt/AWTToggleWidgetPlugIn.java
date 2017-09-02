package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.Checkbox;
import java.awt.ItemSelectable;
import java.awt.event.ItemListener;

/**
 * You'll need to override {@link #setTitle(String)} {@link #setSelected(boolean)} and {@link #getTitle()} if you extend this for other Components.
 * Supports {@link Checkbox}.
 */
public class AWTToggleWidgetPlugIn extends AbstractToggleWidgetPlugIn {

	public AWTToggleWidgetPlugIn() {
		super();
	}
	
	protected final ItemSelectable getJToggleButton() {
		return (ItemSelectable) getWidget();
	}

	@Override
	protected final void addItemListener(ItemListener l) {
		getJToggleButton().addItemListener(l);
	}

	@Override
	public final boolean isSelected() {
		return getJToggleButton().getSelectedObjects()!=null;
	}

	@Override
	protected final void removeItemListener(ItemListener l) {
		getJToggleButton().removeItemListener(l);
	}

	public void setSelected(boolean s) {
		ItemSelectable o = getJToggleButton();
		if(o instanceof Checkbox) {
			((Checkbox)o).setState(s);
			return;
		}
		throw new UnsupportedOperationException(o.getClass().getName() + " not supported.");
	}
	
	public String getTitle() {
		ItemSelectable o=getJToggleButton();
		if(o instanceof Checkbox)
			return ((Checkbox)o).getLabel();
		throw new UnsupportedOperationException(o.getClass().getName() + " not supported.");
	}
	
	public void setTitle(String s){
		ItemSelectable o=getJToggleButton();
		if(o instanceof Checkbox){
			((Checkbox)o).setLabel(s);
			return;
		}
		throw new UnsupportedOperationException(o.getClass().getName() + " not supported.");
	}
}
