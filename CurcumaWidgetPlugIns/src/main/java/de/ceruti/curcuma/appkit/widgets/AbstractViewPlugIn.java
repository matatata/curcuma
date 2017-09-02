package de.ceruti.curcuma.appkit.widgets;

import de.ceruti.curcuma.api.appkit.view.NSView;
import de.ceruti.curcuma.appkit.view.NSViewBase.ViewPlugIn;

public abstract class AbstractViewPlugIn implements ViewPlugIn {

	private ViewPlugIn.Delegate delegate;
	private NSView view;

	protected AbstractViewPlugIn() {
	}

	protected void breakViewConnection() {
	}

	protected void establishViewConnection() {
	}

	public Delegate getDelegate() {
		return this.delegate;
	}

	public NSView getView() {
		return view;
	}

	public abstract Object getViewWidget();

	public abstract boolean isEnabled();

	public abstract boolean isVisible();

	public void setDelegate(Delegate obj) {
		this.delegate = obj;
	}

	public abstract void setEnabled(boolean b);

	public final void setView(NSView view) {
			this.view = view;
	}

	public abstract void setVisible(boolean b);

	protected abstract void _setViewWidget(Object w);

	public final void setViewWidget(Object w){
		if (getViewWidget() != w) {
			if (getViewWidget() != null)
				breakViewConnection();
			
			
			_setViewWidget(w);
			if(w != null)
				establishViewConnection();
		}
	}
	
	public boolean isDummy(){
		return false;
	}
	
}
