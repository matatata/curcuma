package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.Component;

import de.ceruti.curcuma.appkit.view.NSViewBase.ViewPlugIn;
import de.ceruti.curcuma.appkit.widgets.AbstractViewPlugIn;

public class AWTViewPlugIn extends AbstractViewPlugIn implements ViewPlugIn {
	private Component component;

	public AWTViewPlugIn(Component container) {
		this.component = container;
	}

	@Override
	public Component getViewWidget() {
		return component;
	}

	@Override
	protected void _setViewWidget(Object w) {
		this.component = (Component) w;
	}

	@Override
	public boolean isEnabled() {
		return getViewWidget().isEnabled();
	}

	@Override
	public boolean isVisible() {
		return getViewWidget().isVisible();
	}

	@Override
	public void setEnabled(boolean b) {
		getViewWidget().setEnabled(b);
	}

	@Override
	public void setVisible(boolean b) {
		getViewWidget().setVisible(b);
	}
}
