package de.ceruti.curcuma.appkit.view;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSDefaultPlaceholders;
import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.api.appkit.view.NSControl;
import de.ceruti.curcuma.api.appkit.view.cells.NSActionCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptionDescription;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingException;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.appkit.NSPlaceholdersSupport;
import de.ceruti.curcuma.keyvaluebinding.KVCBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;

public class NSControlBase extends NSViewBase implements NSControl, NSDefaultPlaceholders {
	private static Category logger = Logger.getInstance(NSControlBase.class);
	//Experimental
	public static final String ImmediatelyValidateAgainstSubjectControlValueBindingOption = "immediatelyValidateBinding";

	
	private NSCell cell;
	private NSCell.Delegate cellDelegate;
	private Object controlValue;
	private NSDefaultPlaceholders placeholders = new NSPlaceholdersSupport();

	public NSControlBase() {
		super();
		exposeBinding(ControlValueBinding, Object.class);
		addBindingOptionDescriptionForBinding(
				ControlValueBinding,
				new BindingOptionDescription(
						ImmediatelyValidateAgainstSubjectControlValueBindingOption,
						Boolean.class,
						"Validates against subjects as the control value changes.",
						Boolean.TRUE));
		exposeBinding(EditableBinding, Object.class);
		
	}

	private boolean continuouslyCommit = false;
	
	private boolean validateAgainstModel = false;
	
	public boolean isValidateAgainstModel() {
		return validateAgainstModel;
	}

	public void setValidateAgainstModel(boolean validateAgainstModel) {
		this.validateAgainstModel = validateAgainstModel;
	}

	public boolean isContinuouslyCommit() {
		return continuouslyCommit;
	}
	
	public void setContinuouslyCommit(boolean b) {
		this.continuouslyCommit = b;
	}
	
	private NSSelectionMarker isMarkerState = null;

	protected final NSSelectionMarker isMarkerState() {
		return isMarkerState;
	}

	protected final void setMarkerState(NSSelectionMarker isMarkerState) {
		this.isMarkerState = isMarkerState;
	}
	
	private int originalEditableState = -1;


	protected final void conditionallySetEditable(Object newValue) {
		if (newValue instanceof NSSelectionMarker) {
			if (originalEditableState == -1)
				originalEditableState = isEnabled() ? 1 : 0;

			if (newValue.equals(NSSelectionMarker.NSMultipleValuesMarker)) {
				// use original status - so null will be editable!
				setEnabled(originalEditableState == 1 ? true : false);
			} else if (newValue
					.equals(NSSelectionMarker.NSNoSelectionMarker)
					|| newValue
							.equals(NSSelectionMarker.NSNotApplicableMarker)) {
				setEnabled(false);
			} else if (newValue.equals(NSSelectionMarker.NSNullValueMarker)) {
				// use original status - so null will be editable!
				setEnabled(originalEditableState == 1 ? true : false);
			}

			setMarkerState((NSSelectionMarker) newValue);
		} else {

			setMarkerState(null);

			if (originalEditableState != -1)
				setEnabled(originalEditableState == 1 ? true : false);
		}

	}

	
	protected final class ControlValueBinder extends KVCBinder {

		
		public ControlValueBinder(KeyValueCoding kvc) {
			super(kvc);
		}

		

		@Override
		protected void setBindingValue(Object v)
				throws KeyValueBindingSyncException {
			boolean save = isValidateAgainstModel();
			try {
				setValidateAgainstModel(false);
				setControlValue(v);
			} catch (IllegalArgumentException e) {
				throw new KeyValueBindingSyncException(e);
			}
			finally {
				setValidateAgainstModel(save);
			}
		}

		@Override
		protected Object getBindingValue()
				throws KeyValueBindingSyncException {
			return getControlValue();
		}

		@Override
		protected Object validateBinding(Object newValue)
				throws ValidationException,
				KeyValueBindingSyncException {
			boolean save = isValidateAgainstModel();
			try {
				setValidateAgainstModel(false);
				return validateControlValue(newValue);
			} finally {
				setValidateAgainstModel(save);
			}
		}
	}

	
	protected void handleKVBSyncException(KeyValueBindingSyncException e, StringBuffer err) {
		if(err!=null)
			err.append(e.getCause() instanceof ValidationException ? e.getCause().getMessage() : e.getMessage());
	}
	
	protected class NSCellDelegate implements NSCell.Delegate, NSActionCell.Delegate {

		public NSCellDelegate()
		{
		}
		@Override
		public boolean hasValidCellValue(boolean cellState) {
			return cellState;
		}
		
		public void cellDidCancelEditing(NSEditorCell sender) {
			if(sender != getCell())	{
				logger.warn("unknown sender " + sender);
			}
			
			updateCellValue();
		}
		
		@Override
		public boolean cellShouldAcceptInvalidValue(NSCell sender,
				Object invalidValue, String errMsg) {

			if(sender != getCell()) {
				logger.warn("unknown sender " + sender);
			}
			
			if (isMarkerState() != null
					&& ObjectUtils.equals(invalidValue, controlValue)) {
				logger.debug("cell should accept marker " + isMarkerState());
				return true;
			}

			return false;
		}
		
		@Override
		public boolean cellShouldAcceptInvalidPartialValue(NSCell sender,
				Object invalidValue, String errMsg) {
			if(sender != getCell()) {
				logger.warn("unknown sender " + sender);
			}
			
			if (isMarkerState() != null 
					) {
				logger.debug("cell should accept marker " + isMarkerState()
						+ " (display)");
				return true;
			}

			return false;
		}
		
		protected void performContinousCommit(NSCell sender) throws KeyValueBindingSyncException {
			if(sender != getCell()) {
				logger.warn("unknown sender " + sender);
			}
			updateObservableBoundToBinding(ControlValueBinding);
		}

		@Override
		public final void cellValueDidChangeViaGUI(NSCell sender) {
			if(sender != getCell()) {
				logger.warn("unknown sender " + sender);
			}
			
			try {
				updateControlValue(sender);
				if(isContinuouslyCommit())
					performContinousCommit(sender);
			} catch (ValidationException e) {
				logger.error(e);
				getCell().notifyMessage(e.getMessage());
			} catch (KeyValueBindingSyncException e) {
				logger.error(e);
				StringBuffer err = new StringBuffer();
				handleKVBSyncException(e, err);
				getCell().notifyMessage(err.toString());
			}
		}
		


		@Override
		public boolean cellShouldPerformAction(NSActionCell sender) {
			if(sender != getCell()) {
				logger.warn("unknown sender " + sender);
			}

			return checkValidValuesAndNotify(sender);
		}
		
		protected boolean checkValidValuesAndNotify(NSCell sender) {
			boolean ret = sender.hasValidCellValue();
			if(!ret){
				getCell().notifyMessage(sender.getValidationFailureMessage());
				return false;
			}
			
			ret = hasValidControlValue();
			if(!ret){
				getCell().notifyMessage(getValidationFailureMessage());
				return false;
			}
			
			return true;
		}

		@Override
		public void cellDidPerformAction(NSActionCell sender) {
			if(sender != getCell()) {
				logger.warn("unknown sender " + sender);
			}
			
			logger.debug("cellDidPerformAction()");
		}

		@Override
		public Object cellValidateCellValue(NSCell sender, Object c) throws ValidationException {
			if(sender != getCell()) {
				//the sender usually will be the editor-cell
				logger.warn("unknown sender " + sender);
			}
			
			return c;
		}
		
		@Override
		public Object cellValidateDisplayValue(NSCell sender, Object obj) throws ValidationException {
			return obj;
		}
	}

	/**
	 * Overide this if you want to provide another default Delegate
	 * @return
	 */
	protected NSCell.Delegate createDefaultCellDelegate() {
		return new NSCellDelegate(); 
	}

	@Override
	public void setCell(NSCell cell) {
		if(this.cell!=null && cell==null) {
			this.cell.setDelegate(null);
		}

		this.cell = cell;

		if(cell!=null) {
			if(cellDelegate==null)
				cellDelegate = createDefaultCellDelegate();

			if(cellDelegate!=null)
				cell.setDelegate(cellDelegate);
		}
	}

	@Override
	public NSCell getCell() {
		return this.cell;
	}

	@Override
	public final Object getControlValue() {
		return controlValue;
	}

	@Override
	public final void setControlValue(Object ctrlVal) {
		logger.debug("setControlValue(" + ctrlVal + ")");
		try {
			_setControlValue(ctrlVal);
		} catch (ValidationException e) {
			throw new IllegalArgumentException("setControlValue(" + ctrlVal + ")",e);
		}
		
		updateCellValue();
	}


	/**
	 * take the control-value and make this control's cell reflect it.
	 */
	protected final void updateCellValue() {
		Object val = getControlValue();
			getCell().setCellValue(val);
		
	}
	

	@Override
	public boolean isEditable() {
		return getCell().isEditable();
	}

	@Override
	public void setEditable(boolean b) {
		getCell().setEditable(b);
	}

	public final void updateControlValue() throws ValidationException {
		updateControlValue(getCell());
	}
	
	protected final void updateControlValue(NSCell cell) throws ValidationException {
		_setControlValue(cell.getCellValue());
	}
	
	private boolean hasValidControlValue = false;
	private String validationFailureMessage;
	public boolean hasValidControlValue() {
		return hasValidControlValue;
	}
	
	public String getValidationFailureMessage() {
		return validationFailureMessage;
	}

	protected final void _setControlValue(Object newCtrlVal)
			throws ValidationException {
		Object tmp = newCtrlVal;
		
		conditionallySetEditable(newCtrlVal);
		
		try {
			hasValidControlValue = false;
			if (tmp instanceof NSSelectionMarker) {
				tmp = Utils.getPlaceholderForMarker(NSControlBase.this,
						NSControlBase.this, ControlValueBinding,
						(NSSelectionMarker) tmp);
				hasValidControlValue = false;
				validationFailureMessage = "Marker-State! TODO: think about it";
			} else {
				tmp = validateControlValue(tmp);
				hasValidControlValue = true;
			}
		} catch (ValidationException e) {
			// could ask delegate
			logger.debug(e);
			validationFailureMessage = e.getMessage();
			throw e;
		}

		try {
			willChangeValueForKey("controlValue");
			this.controlValue = tmp;
		} finally {
			didChangeValueForKey("controlValue");
		}

	}
	
	@Override
	public Object validateControlValue(Object c) throws ValidationException {
		if(c instanceof NSSelectionMarker)
			return c;
		
		if(!isValidateAgainstModel())
			return c;
		
		try {
			return validateSubject(c,ControlValueBinding);
		} 
		catch (ValidationException e) {
			throw e;
		} catch (KeyValueBindingSyncException e) {
			throw new KeyValueBindingException(e);
		}
	}
	


	

	@Override
	public KeyValueBinder createBinderForBinding(String binding, Object observedObject)
	{
		if (binding.equals(ControlValueBinding)) {
			return new ControlValueBinder(this);
		}
		
		return super.createBinderForBinding(binding,observedObject);
	}

	@Override
	public Object getDefaultPlaceholderForMarkerWithBinding(
			NSSelectionMarker marker, String binding) {
		return placeholders.getDefaultPlaceholderForMarkerWithBinding(marker,
				binding);
	}

	@Override
	public void setDefaultPlaceholderForMarkerWithBinding(Object placeholder,
			NSSelectionMarker marker, String binding) {
		placeholders.setDefaultPlaceholderForMarkerWithBinding(placeholder,
				marker, binding);
	}


}
