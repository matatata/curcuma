package de.ceruti.curcuma.appkit.view.cells;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell.Delegate;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvaluebinding.AbstractScalarValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

public abstract class AbstractCellBase extends NSObjectImpl implements NSCell,
		WidgetPlugin.Delegate {
	private static Category logger = Logger.getInstance(AbstractCellBase.class);
	
	
	private WidgetPlugin plugin;
	private Object cellValue;
	private NSCell.Delegate delegate = null;
	
	@Override
	public final void setDisplayValue(Object v) {
		try {
			try {
				v = validateDisplayValue(v);
			} catch (ValidationException e) {
				if (!getDelegate().cellShouldAcceptInvalidPartialValue(this,
						v, e.getMessage()))
					throw e;
			}
			logger.debug("setDisplayValue(" + v + ")");
			_setPlugInValue(v);

		} catch (ValidationException e) {
			throw new IllegalArgumentException();
		}
	}
	
	@Override
	public final Object getDisplayValue() {
		return getWidgetPlugIn().getPlugInValue();
	}
	
	@Override
	public Object validateDisplayValue(Object obj) throws ValidationException{
		return getWidgetPlugIn().validatePlugInValue(getDelegate().cellValidateDisplayValue(this, obj));
	}
	
	protected AbstractCellBase(){
		exposeBinding(CellValueBinding,Object.class);
		exposeBinding(CellEnabledBinding,Boolean.class);
	}
	
	@Override
	public final void setWidgetPlugIn(WidgetPlugin w){
		this.plugin = w;
		this.plugin.setWidgetAssociation(this);
	}
	
	public WidgetPlugin getWidgetPlugIn() {
		return plugin==null ? WidgetPlugin.Dummy.INSTANCE : plugin;
	}
	
	@Override
	public Object getImplementation() {
		return getWidgetPlugIn();
	}
	

	@Override
	public final boolean isVisible() {
		return getWidgetPlugIn().isVisible();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.cells.NSCell#hasFocus()
	 */
	@Override
	public final boolean hasFocus() {
		return getWidgetPlugIn().hasFocus();
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.cells.NSCell#requestFocus()
	 */
	@Override
	public final void requestFocus() {
		getWidgetPlugIn().requestFocus();
	}
	
	@Override
	public final void setVisible(boolean b) {
		getWidgetPlugIn().setVisible(b);
	}

	@Override
	public final boolean isEnabled() {
		return getWidgetPlugIn().isEnabled();
	}
	
	@Override
	@PostKVONotifications
	public final void setEnabled(boolean b) {
		getWidgetPlugIn().setEnabled(b);
		try {
			updateObservableBoundToBinding(CellEnabledBinding);
		} catch (KeyValueBindingSyncException e) {
			throw new KeyValueBindingException(e);
		}
	}
	
	@Override
	@PostKVONotifications
	public void setBackground(Object nativeColor) {
		getWidgetPlugIn().setBackground(nativeColor);
	}
	
	@Override
	public Object getBackground() {
		return getWidgetPlugIn().getBackground();
	}
	
	@Override
	@PostKVONotifications
	public void setForeground(Object nativeColor) {
		getWidgetPlugIn().setForeground(nativeColor);
	}
	
	@Override
	public Object getForeground() {
		return getWidgetPlugIn().getForeground();
	}

	/**
	 * Validates <code>value</code> using the plugins's
	 * validatePlugInValue(Object) method and calls
	 * <code>getWidgetPlugIn().setPlugInValue(value)</code>. You may override
	 * this in order to convert <code>value</code> before passing it to the
	 * inherited implementation.
	 * 
	 * @param value
	 *            the plugin's new value i.e. the cell's current value.
	 * @throws ValidationException
	 *             if the plugin rejects <code>value</code>
	 */
	protected final void _setPlugInValue(Object value) throws ValidationException{
		value = getWidgetPlugIn().validatePlugInValue(value);
		getWidgetPlugIn().setPlugInValue(value);
	}
	
	
	@Override
	public void updateDisplayValue() {
		Object cellVal = getCellValue();
		Object displayVal = cellVal;
		try {
			displayVal = cell2DisplayValue(cellVal);
		} catch (ValidationException e) {
			if(!cannotConvertToDisplayValue(cellVal,e.getMessage())){
				return;
			}
		}
		
		setDisplayValue(displayVal);
		
	}

	/**
	 * Invoked if the cell value could not be converted to a valid display value.
	 * @param val
	 * @param errMsg
	 * @return true if the value should be used with {@link #setDisplayValue(Object)} anyway.
	 */
	protected boolean cannotConvertToDisplayValue(Object val,String errMsg) {
		logger.debug(errMsg);
		return true;
	}

//	public final Delegate getDelegate() {
//		return delegate;
//	}
	
	@Override
	public final void setDelegate(NSCell.Delegate d) {
		this.delegate = d;
	}
	
	
	@Override
	public NSCell.Delegate getDelegate(){
		Object del = delegate;
		if(del!=null && (del instanceof NSCell.Delegate))
			return (NSCell.Delegate) del;
		return NSCell.Delegate.Dummy.INSTANCE;
	}
	
	public NSEditorCell.Delegate getEditorDelegate() {
		Object del = getDelegate();
		if(del!=null && del instanceof NSEditorCell.Delegate)
			return (NSEditorCell.Delegate)del;
		return NSEditorCell.Delegate.Dummy.INSTANCE;
	}

	@Override
	public KeyValueBinder createBinderForBinding(String binding, Object observedObject) {
		if (binding.equals(CellValueBinding))
			return new AbstractScalarValueBinder() {
				@Override
				protected Object getBindingValue()
						throws KeyValueBindingSyncException {
					return getCellValue();
				}

				@Override
				protected void setBindingValue(Object newValue)
						throws KeyValueBindingSyncException {
					try {
						setCellValue(newValue);
					} catch (IllegalArgumentException e) {
						throw new KeyValueBindingSyncException(e);
					}
				}

				@Override
				protected Object validateBinding(Object newValue)
						throws ValidationException,
						KeyValueBindingSyncException {
					return validateCellValue(newValue);
				}
			};
		return super.createBinderForBinding(binding,observedObject);
	}
	
	@Override
	public void setValidityStatus(boolean isValid, String reason) {
		try {
			getWidgetPlugIn().setValidityStatus(isValid, reason);
		} catch (NullPointerException e) {
			logger.error("This is probably a table editor cell, without a widget. Do we really want to support that? TODO remove it. If you use an editor-cell, please provide a widget.");
		}
	}
	
	@Override
	public boolean plugInValueDidChangeViaGui(WidgetPlugin sender) {
		try {
			if(updateCellValue()){
				getDelegate().cellValueDidChangeViaGUI(this);
				updateObservableBoundToBinding(CellValueBinding);
				return true;
			}
		} catch (KeyValueBindingSyncException e) {
			throw new KeyValueBindingException(e);
		}
		return false;
	}
	
	@Override
	public final Object getCellValue() {
		return cellValue;
	}

//	public final void setMarkerValue(Object v){
//		this.cellValue = v;
//		setMarkerState(true);
//	}

	
//	/**
//	 * Try to update the cell value by taking the value from the gui
//	 * @throws ValidationException TODO
//	 */
//	protected final void updateCellValue(Object val) throws ValidationException {
//		_setCellValue(val);
//		getDelegate().cellValueDidChange(this);
//	}
	
	
	protected Object display2cellValue(Object d) throws ValidationException {
		return d;
	}
	
	protected Object cell2DisplayValue(Object c) throws ValidationException {
		return c;
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSCell#updateCellValue()
	 */
	@Override
	public final boolean updateCellValue() {
		validCellValue=false;
		try {
			Object c = display2cellValue(getDisplayValue());
			_setCellValue(c);
//			if(notifyDelegate)
//				getDelegate().cellValueDidChangeViaGUI(this);
		} catch (ValidationException e) {
			logger.debug(e);
			validationFailureMessage = e.getMessage();
		}
		boolean valid = hasValidCellValue();
	
		setValidityStatus(valid, valid ? null : validationFailureMessage);
		
		return valid;
	}
	

	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.cells.NSCell#setCellValue(java.lang.Object)
	 */
	@Override
	public final void setCellValue(Object cellValue) {
		try {
			_setCellValue(cellValue);
			setValidityStatus(true, null);
			
		} catch (ValidationException e) {
			throw new IllegalArgumentException("setCellValue(" + cellValue + ")",e);
		}
		updateDisplayValue();
	}
	
	
	
	private boolean validCellValue=false;
	private String  validationFailureMessage;
		
	@Override
	public boolean hasValidCellValue() {
		return getDelegate().hasValidCellValue(validCellValue);
	}

	@Override
	public String getValidationFailureMessage() {
		return validationFailureMessage;
	}

	/**
	 * Tries to really set the cellValue, validating <code>cellValue</code> using {@link #validateCellValue(Object)}.
	 * If validation succeeds <code>validCellValue</code> will be set to true.
	 * If validation fails, the Delegate has a chance to force the cell to accept the invalid <code>cellValue</code>.
	 *
	 * Posts KVO-Notifications about the cellValue-Key.
	 * Does not update the Display-Value!
	 * @param cellValue
	 * @throws ValidationException if validation failed and cellValue (valid or invalid) has not been accepted
	 * @see Delegate.cellShouldAcceptInvalidValue
	 **/
	private final void _setCellValue(Object cellValue) throws ValidationException
	{

		validCellValue=false;
		Object tmp=cellValue;
		try {
			tmp = validateCellValue(cellValue);
			validCellValue=true;
			logger.debug("cellValue="+ tmp + " is valid");
		} catch (ValidationException e) {
			validationFailureMessage = e.getMessage();
			if(!getDelegate().cellShouldAcceptInvalidValue(this, cellValue, e.getMessage())){
				throw e;
			}
		}
		
		if(ObjectUtils.equals(this.cellValue, tmp))
			return;
		
		try {
			willChangeValueForKey("cellValue");
			logger.debug("setCellValue(" + cellValue + ")");
			this.cellValue = tmp;
		} finally {
			didChangeValueForKey("cellValue");
		}
	}



	/**
	 * Make sure the given value <code>cellValue</code> can be used as this cell's new value. If necessary
	 * and possible it can be converted into another value.
	 * @param cellValue
	 *            value
	 * @return new cell value or <code>cellValue</code> if no change was
	 *         necessary.
	 * @throws ValidationException
	 *             if <code>cellValue</code> could not be verified i.e. it is
	 *             not a valid cell value and could not be converted to one.
	 */
	@Override
	public Object validateCellValue(Object v)
			throws ValidationException {
		return getDelegate().cellValidateCellValue(this, v);
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSCell#notifyMessage(java.lang.String)
	 */
	@Override
	public final void notifyMessage(String m) {
		getWidgetPlugIn().notifyMessage(m);
	}

	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSCell#isEditable()
	 */
	@Override
	public final boolean isEditable() {
		return getWidgetPlugIn().isEditable();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSCell#setEditable(boolean)
	 */
	@Override
	public final void setEditable(boolean e) {
		getWidgetPlugIn().setEditable(e);
	}
	
	
	
	
	/**
	 * 
	 * @param errMessage will be filled in case the input is not valid input.
	 * @see de.ceruti.curcuma.api.appkit.view.cells.NSCell#isValidCellValue(java.lang.StringBuffer)
	 */
	protected final boolean isValidEdit(StringBuffer errMessage) {
		
		if(!hasValidCellValue())
			return false;
		try {
			validateDisplayValue(getDisplayValue());
			return true;
		} catch (ValidationException e) {
			if (errMessage != null)
				errMessage.append(e.getMessage());
			logger.warn(e);
			return false;
		}
		
		
		
//		if(!isDirty()){
//			return !(getCellValue() instanceof NSSelectionMarker);
//		}
//		
//		try {
//			Object newValue = validateCellValue(getWidgetPlugIn().getPlugInValue());
//			newValue = getNSCellDelegate().validateCellValue(newValue);
//			return true;
//		} catch (ValidationException e) {
//			if (errMessage != null)
//				errMessage.append(e.getMessage());
//			logger.warn(e);
//			return false;
//		}
	}
	
	
	
	

}
