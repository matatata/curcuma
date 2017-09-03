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

	@Override
	public Delegate getDelegate() {
		return this.delegate;
	}

	@Override
	public NSView getView() {
		return view;
	}

	@Override
	public abstract Object getViewWidget();

	@Override
	public abstract boolean isEnabled();

	@Override
	public abstract boolean isVisible();

	@Override
	public void setDelegate(Delegate obj) {
		this.delegate = obj;
	}

	@Override
	public abstract void setEnabled(boolean b);

	@Override
	public final void setView(NSView view) {
			this.view = view;
	}

	@Override
	public abstract void setVisible(boolean b);

	protected abstract void _setViewWidget(Object w);

	@Override
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
