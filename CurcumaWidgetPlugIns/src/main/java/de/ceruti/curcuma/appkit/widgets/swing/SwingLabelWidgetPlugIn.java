package de.ceruti.curcuma.appkit.widgets.swing;

import javax.swing.JLabel;

import de.ceruti.curcuma.appkit.widgets.awt.AWTWidgetPlugIn;

public class SwingLabelWidgetPlugIn extends AWTWidgetPlugIn {

	public SwingLabelWidgetPlugIn() {
		super();
	}
	
	public SwingLabelWidgetPlugIn(JLabel label) {
		setWidget(label);
	}

	@Override
	public JLabel getWidget() {
		return (JLabel) super.getWidget();
	}

	@Override
	public Object getPlugInValue() {
		return getWidget().getText();
	}

	@Override
	protected void _setPlugInValue(Object p) {
		getWidget().setText(p == null ? null : p.toString());
	}

	@Override
	public void setValidityStatus(boolean isValid, String reason) {
	}
}
