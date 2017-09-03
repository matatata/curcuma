package de.ceruti.curcuma.appkit.widgets.swing;

import javax.swing.BoundedRangeModel;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.ceruti.curcuma.api.core.ValueTransformer;
import de.ceruti.curcuma.api.core.exceptions.ConversionException;
import de.ceruti.curcuma.appkit.view.cells.BoundedRangeWidgetPlugin;
import de.ceruti.curcuma.appkit.widgets.awt.AWTWidgetPlugIn;
import de.ceruti.curcuma.core.Values;

public class SwingBoundedRangeWidgetPlugIn extends AWTWidgetPlugIn implements BoundedRangeWidgetPlugin{

	private final MyModel changeListenerModel;
	
	private class MyModel extends DefaultBoundedRangeModel implements ChangeListener{
		private static final long serialVersionUID = 1L;

		public MyModel() {
			addChangeListener(this);
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			if (!notifyAssociationEnabled())
				return;
			getWidgetAssociation().plugInValueDidChangeViaGui(SwingBoundedRangeWidgetPlugIn.this);
			actionPerformed();
		}
	}
	
	@Override
	public void setValidityStatus(boolean isValid, String reason) {
	}
	

	
	@Override
	public BoundedRangeWidgetPlugin.Delegate getWidgetAssociation() {
		Object assoc = super.getWidgetAssociation();
		if (assoc instanceof BoundedRangeWidgetPlugin.Delegate)
			return (BoundedRangeWidgetPlugin.Delegate) assoc;
		return BoundedRangeWidgetPlugin.Delegate.Dummy.INSTANCE;
	}
	
	@Override
	public void setMaximumPlugInValue(int v) {
		changeListenerModel.setMaximum(v);
	}

	@Override
	public void setMinimumPlugInValue(int v) {
		changeListenerModel.setMinimum(v);
	}
	
	@Override
	public int getMaximumPlugInValue() {
		return changeListenerModel.getMaximum();
	}

	@Override
	public int getMinimumPlugInValue() {
		return changeListenerModel.getMinimum();
	}
	
	@Override
	public void breakCellConnection() {
		super.breakCellConnection();

		setModel(null);
	}
	
	public SwingBoundedRangeWidgetPlugIn(){
		changeListenerModel = new MyModel();
	}
	
	
	@Override
	public void establishCellConnection() {
		super.establishCellConnection();
		setModel(changeListenerModel);
	}

	private void setModel(BoundedRangeModel model){
		Object widget = getWidget();
		if(widget instanceof JSlider) {
			((JSlider)widget).setModel(model);
			return;
		}
		else if(widget instanceof JProgressBar){
			((JProgressBar)widget).setModel(model);
			return;
		}
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getPlugInValue() {
		ValueTransformer t = ValueTransformer.Identity;
		if(getWidgetAssociation().getBoundedRangeTransformer()!=null){
			t=getWidgetAssociation().getBoundedRangeTransformer();
		}
		
		try {
			return t.reverseTransformedValue(changeListenerModel.getValue());
		} catch (ConversionException e) {
			throw new IllegalStateException(e);
		}
	}

	@Override
	protected void _setPlugInValue(Object v) {
		ValueTransformer t = ValueTransformer.Identity;
		if(getWidgetAssociation().getBoundedRangeTransformer()!=null){
			t=getWidgetAssociation().getBoundedRangeTransformer();
		}
		try {
			setValue(Values.number(t.transformedValue(v)).intValue());
		} catch (ConversionException e) {
			throw new IllegalStateException(e);
		}
		
	}

	@Override
	public int getValue() {
		return changeListenerModel.getValue();
	}

	@Override
	public void setValue(int v) {
		changeListenerModel.setValue(v);
	}

	

	
}
