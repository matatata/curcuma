package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.ItemSelectable;

import javax.swing.AbstractButton;

import de.ceruti.curcuma.appkit.widgets.awt.AWTToggleWidgetPlugIn;

public class SwingToggleWidgetPlugIn extends AWTToggleWidgetPlugIn {

	public SwingToggleWidgetPlugIn() {
		super();
	}

	@Override
	public void setSelected(boolean s) {
		ItemSelectable o = getJToggleButton();
		if(o instanceof AbstractButton){
			((AbstractButton)o).setSelected(s);
			return;
		}
		super.setSelected(s);
	}
	
	@Override
	public String getTitle() {
		ItemSelectable o=getJToggleButton();
		if(o instanceof AbstractButton)
			return ((AbstractButton)o).getText();
		return super.getTitle();
	}
	
	@Override
	public void setTitle(String s){
		ItemSelectable o=getJToggleButton();
		if(o instanceof AbstractButton){
			((AbstractButton)o).setText(s);
			return;
		}
		super.setTitle(s);
	}

}
