
package de.ceruti.curcuma.appkit.view;


import de.ceruti.curcuma.api.appkit.view.NSView;
import de.ceruti.curcuma.foundation.NSObjectImpl;

public abstract class NSViewBase extends NSObjectImpl implements NSView {
	
	/**
	 * GUI-Layer
	 */
	public interface ViewPlugIn {
		NSView getView();
		Object getViewWidget();
		boolean isVisible();
		void setVisible(boolean b);
		boolean isEnabled();
		void setEnabled(boolean b);
		
		void setView(NSView view);
		void setViewWidget(Object w);
		
		static interface Delegate {
			class Dummy implements Delegate {
				public static final Delegate INSTANCE = new Dummy();
			}
		}
		
		void setDelegate(Delegate obj);
		Delegate getDelegate();
		
		class Dummy implements ViewPlugIn {

			@Override
			public NSView getView() {
				return null;
			}

			@Override
			public Object getViewWidget() {
				return null;
			}

			@Override
			public boolean isVisible() {
				return false;
			}

			@Override
			public void setVisible(boolean b) {
			}

			@Override
			public boolean isEnabled() {
				return false;
			}

			@Override
			public void setEnabled(boolean b) {
			}

			@Override
			public void setView(NSView view) {
			}

			@Override
			public void setViewWidget(Object w) {
			}

			@Override
			public void setDelegate(Delegate obj) {
			}

			@Override
			public Delegate getDelegate() {
				return null;
			}
			
		}
	}

	private ViewPlugIn viewPlugIn;

	protected NSViewBase() {
	}

	public ViewPlugIn getViewPlugIn() {
		return viewPlugIn;
	}

	public void setViewPlugIn(ViewPlugIn viewPlugIn) {
		if(this.viewPlugIn!=null && viewPlugIn==null){
			this.viewPlugIn.setView(null);
			this.viewPlugIn.setDelegate((ViewPlugIn.Delegate)null);
		}
		
		this.viewPlugIn = viewPlugIn;
		if(viewPlugIn!=null) {
			viewPlugIn.setView(this);
			this.viewPlugIn.setDelegate(createDefaultViewPlugInDelegate());
		}
	}

	
	
	/**
	 * Overide this if you want to provide another default Delegate
	 * @return
	 */
	protected ViewPlugIn.Delegate createDefaultViewPlugInDelegate() {
		return ViewPlugIn.Delegate.Dummy.INSTANCE; 
	}
	
	@Override
	public void setVisible(boolean b) {
		getViewPlugIn().setVisible(b);
	}
	
	@Override
	public boolean isVisible() {
		return getViewPlugIn().isVisible();
	}

	@Override
	public boolean isEnabled() {
		return getViewPlugIn().isEnabled();
	}

	@Override
	public void setEnabled(boolean b) {
		getViewPlugIn().setEnabled(b);
	}

	

}
