package de.ceruti.curcuma.appkit.view.cells;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.cells.NSActionCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSBoundedRangeCell;
import de.ceruti.curcuma.api.core.ValueTransformer;
import de.ceruti.curcuma.api.core.exceptions.ConversionException;
import de.ceruti.curcuma.core.Values;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;


public class NSActionCellImpl extends AbstractCellBase implements
		NSBoundedRangeCell, NSActionCell, ActionControlWidgetPlugin.Delegate,
		BoundedRangeWidgetPlugin.Delegate {
	private static Category logger = Logger.getInstance(NSActionCellImpl.class);
	
	public NSActionCellImpl() {
		super();
	}
	
	
	@Override
	public NSActionCell.Delegate getActionDelegate(){
		Object del = super.getDelegate();
		if(del!=null && (del instanceof NSActionCell.Delegate))
			return (NSActionCell.Delegate) del;
		return NSActionCell.Delegate.Dummy.INSTANCE;
	}

	@Override
	public void pluginDidPerformAction(ActionControlWidgetPlugin sender) {
		logger.debug("pluginDidPerformAction()");
		getActionDelegate().cellDidPerformAction(this);
	}

	@Override
	public final boolean pluginShouldPerformAction(ActionControlWidgetPlugin sender) {
		return getActionDelegate().cellShouldPerformAction(this);
	}

	
	private Object[] cellValueRange = new Object[]{UNBOUNDED,UNBOUNDED};
	private int resolution = -1;

	@Override
	public Object[] getCellValueRange() {
		return cellValueRange;
	}
	
	@Override
	public int getResolution() {
		return resolution;
	}


	@Override
	public void setResolution(int resolution) {
		if(resolution < -1)
			throw new IllegalArgumentException();
		this.resolution = resolution;
		Object[] range = getCellValueRange();
		if(range[0]!=UNBOUNDED && range[1] != UNBOUNDED)
			setCellValueRange(range);
	}

	@Override
	public ValueTransformer getBoundedRangeTransformer() {
		return def;
	}
	
	private TheValueTransformer def = new TheValueTransformer();
	
	private class TheValueTransformer implements ValueTransformer {
		
		private float scale() throws ConversionException{
			Object [] range = getCellValueRange();
			if(range[0]==UNBOUNDED || range[1]==UNBOUNDED)
				throw new IllegalStateException();
			
			if(getResolution()==-1)
				return 1.0f;
			
			float min,max;
			
			min = Values.number(range[0]).floatValue();
			max = Values.number(range[1]).floatValue();
			
			
			float f =  getResolution()/(max -min);
			return f;
		}
		
		@Override
		public Object transformedValue(Object val) throws ConversionException {

			try {
				if(val == NSBoundedRangeCell.UNBOUNDED)
					throw new IllegalStateException("not bounded");
				
				
				return Values.number(val).floatValue() * scale();
			} catch (ConversionException e) {
				throw new IllegalArgumentException();
			}
			
		}
		
		@Override
		public Object reverseTransformedValue(Object val)
				throws ConversionException {
			
			return Values.number(val).floatValue()/scale();
		}
		
		@Override
		public boolean allowsReverseTransformation() {
			return true;
		}
	};
	
	@Override
	@PostKVONotifications
	public void setCellValueRange(Object []cellValueRange) {
		this.cellValueRange = cellValueRange;
		setMinimumCellValue(cellValueRange[0]);
		setMaximumCellValue(cellValueRange[1]);
		
	}
	
	private void setMinimumCellValue(Object v) {
		
		
		try {
			
			
			int imin= Values.number(getBoundedRangeTransformer().transformedValue(v)).intValue();
			
			((BoundedRangeWidgetPlugin)getWidgetPlugIn()).setMinimumPlugInValue(imin);
		}
		catch(ConversionException e){
			throw new IllegalArgumentException(e);
		}
	}
	
	private void setMaximumCellValue(Object v) {
		try {
			int imax=Values.number(getBoundedRangeTransformer().transformedValue(v)).intValue();
			
			((BoundedRangeWidgetPlugin)getWidgetPlugIn()).setMaximumPlugInValue(imax);
		}catch(ConversionException e){
			throw new IllegalArgumentException(e);
		}
	}
	
	
}
