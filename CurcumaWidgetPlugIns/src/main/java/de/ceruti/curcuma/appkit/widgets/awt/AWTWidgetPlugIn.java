package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.Color;
import java.awt.Component;

import de.ceruti.curcuma.appkit.view.NSViewBase.ViewPlugIn;
import de.ceruti.curcuma.appkit.view.cells.ActionControlWidgetPlugin;
import de.ceruti.curcuma.appkit.widgets.AbstractActionWidgetPlugIn;

public abstract class AWTWidgetPlugIn extends AbstractActionWidgetPlugIn implements
		ActionControlWidgetPlugin, ViewPlugIn {

	private Component component;

	protected AWTWidgetPlugIn() {
		super();
	}

	public Component getWidget() {
		return component;
	}

	@Override
	protected void _setWidget(Object obj) {
		this.component = (Component) obj;
	}
	
	public void setValidityStatus(boolean isValid, String reason) {
	}

	protected void _setViewWidget(Object w) {
	}

	public Component getViewWidget() {
		return getWidget();
	}

	public void requestFocus() {
		getWidget().requestFocusInWindow();
	}

	public boolean hasFocus() {
		return getWidget().hasFocus();
	}

	public abstract Object getPlugInValue();

	public boolean isEnabled() {
		return getWidget().isEnabled();
	}

	public boolean isVisible() {
		return getWidget().isVisible();
	}

	public void setEnabled(boolean e) {
		getWidget().setEnabled(e);
	}

	public void setVisible(boolean v) {
		getWidget().setVisible(v);
	}

	public boolean isEditable() {
		return isEnabled();
	}

	public void setEditable(boolean e) {
		setEnabled(e);
	}
	
	public Object getBackground() {
		return getWidget().getBackground();
	}

	public void setBackground(Object nat) {
		getWidget().setBackground((Color)nat);
	}
	
	public Object getForeground() {
		return getWidget().getForeground();
	}

	public void setForeground(Object nat) {
		getWidget().setForeground((Color)nat);
	}
	
	public void notifyMessage(String m) {
//		Toolkit.getDefaultToolkit().beep();
		// TODO show Message
	}


}
