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

	@Override
	public Component getWidget() {
		return component;
	}

	@Override
	protected void _setWidget(Object obj) {
		this.component = (Component) obj;
	}
	
	@Override
	public void setValidityStatus(boolean isValid, String reason) {
	}

	@Override
	protected void _setViewWidget(Object w) {
	}

	@Override
	public Component getViewWidget() {
		return getWidget();
	}

	@Override
	public void requestFocus() {
		getWidget().requestFocusInWindow();
	}

	@Override
	public boolean hasFocus() {
		return getWidget().hasFocus();
	}

	@Override
	public abstract Object getPlugInValue();

	@Override
	public boolean isEnabled() {
		return getWidget().isEnabled();
	}

	@Override
	public boolean isVisible() {
		return getWidget().isVisible();
	}

	@Override
	public void setEnabled(boolean e) {
		getWidget().setEnabled(e);
	}

	@Override
	public void setVisible(boolean v) {
		getWidget().setVisible(v);
	}

	@Override
	public boolean isEditable() {
		return isEnabled();
	}

	@Override
	public void setEditable(boolean e) {
		setEnabled(e);
	}
	
	@Override
	public Object getBackground() {
		return getWidget().getBackground();
	}

	@Override
	public void setBackground(Object nat) {
		getWidget().setBackground((Color)nat);
	}
	
	@Override
	public Object getForeground() {
		return getWidget().getForeground();
	}

	@Override
	public void setForeground(Object nat) {
		getWidget().setForeground((Color)nat);
	}
	
	@Override
	public void notifyMessage(String m) {
//		Toolkit.getDefaultToolkit().beep();
		// TODO show Message
	}


}
