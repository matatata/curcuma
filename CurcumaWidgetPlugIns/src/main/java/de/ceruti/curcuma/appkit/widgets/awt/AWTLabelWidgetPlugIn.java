package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.Label;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

public class AWTLabelWidgetPlugIn extends AWTWidgetPlugIn {
	private static Category logger = Logger
			.getInstance(AWTLabelWidgetPlugIn.class);

	public AWTLabelWidgetPlugIn() {
		super();
	}

	@Override
	public Label getWidget() {
		return (Label) super.getWidget();
	}

	@Override
	public Object getPlugInValue() {
//		return Values.string(getWidget().getText());
		return getWidget().getText();
	}

	@Override
	protected void _setPlugInValue(Object p) {
		getWidget().setText(p.toString()/*.stringValue()*/);
	}
	
	@Override
	public void setValidityStatus(boolean isValid, String reason) {
	}

}
