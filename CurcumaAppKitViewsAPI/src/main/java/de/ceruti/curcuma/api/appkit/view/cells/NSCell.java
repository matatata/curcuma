package de.ceruti.curcuma.api.appkit.view.cells;

import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;

public interface NSCell extends KeyValueCoding, KeyValueObserving,
		KeyValueBindingCreation {

	String CellValueBinding = "cellValue";
	String CellEnabledBinding = "enabled";
	
	Delegate getDelegate();

	void setDelegate(Delegate d);

	boolean isVisible();

	void setVisible(boolean b);

	boolean isEnabled();

	void setEnabled(boolean b);

	Object getCellValue();
	
	void setValidityStatus(boolean isValid, String reason);

	/**
	 * Determines if the current edit-buffer (i.e. the current user-input) is
	 * valid input.
	 * 
	 * @param errMessage
	 *            Will be filled in case of an invalid edit. You can pass null if
	 *            you are not interested.
	 * @return true if the current input is valid and can be used as or converted
	 *         to this cell's new cell value.
	 */
//	boolean isValidEdit(StringBuffer errMessage);

	/** Tries to really set the cellValue, validating <code>cellValue</code> using {@link #validateCellValue(Object)}.
	 * If validation succeeds <code>validCellValue</code> will be set to true.
	 * If validation fails, the Delegate has a chance to force the cell to accept the invalid <code>cellValue</code>.
	 *
	 * Posts KVO-Notifications about the cellValue-Key, if cellValue changed.
	 * 
	 * Does update the Display-Value using {@link #updateDisplayValue()} if cellValue has been accepted
	 * 
	 * Does not trigger notifications such as the ones beeing triggered when
	 * editing occured from gui.
	 * 
	 * @param cellValue
	 * @throws IllegalArgumentException if validation failed and cellValue has not been accepted
	 * @see de.ceruti.curcuma.api.appkit.view.cells.NSCell#setCellValue(java.lang.Object)
	 */
	void setCellValue(Object t);
	
	/**
	 * Sets the display-value of this cell, validating <code>displayValue</code> by using {@link #validateDisplayValue(Object)}.
	 * If validation fails, the Delegate can force the cell to accept the value.
	 * This cell's cellValue will not be modified!
	 * @param displayValue
	 * @throws IllegalArgumentException if validation failed and displayValue has not been accepted
	 * 
	 */
	void setDisplayValue(Object displayValue);
	
	/**
	 * Check if <code>newDisplayVal</code> can be used as this cell's display-Value.
	 * 
	 * This method should not change this cell's state.
	 * @param newDisplayVal
	 * @return newDisplayVal or a value that should be used instead of <code>newDisplayVal</code>.
	 * @throws ValidationException if <code>newDisplayVal</code> is not acceptable and a replacement could not be found.
	 */
	Object validateDisplayValue(Object newDisplayVal) throws ValidationException;
	
	Object getDisplayValue();
	
//	/**
//	 * Make sure the given value <code>cellValue</code> can be used as this cell's new value. If necessary
//	 * and possible it can be converted into another value.
//	 * 
//	 * @param cellValue
//	 *            value
//	 * @return new cell value or <code>cellValue</code> if no change was
//	 *         necessary.
//	 * @throws VerificationException
//	 *             if <code>cellValue</code> could not be verified i.e. it is
//	 *             not a valid cell value and could not be converted to one.
//	 */
//	Value verifyCellValue(Value cellValue) throws VerificationException;

	/**
	 * Transforms the displayValue ({@link #getDisplayValue()}) into the represented object.
	 * Validates the latter using {@link #validateCellValue(Object)}.
	 * If validation succeeds <code>validCellValue</code> will be set to true.
	 * If validation fails, the Delegate has a chance to force the cell to accept the invalid <code>cellValue</code>.
	 * 
	 * Does not alter the display-value.
	 * Posts KVO-Notifications about the cellValue-Key, if cellValue changed.
	 * @return #hasValidCellValue()
	 */
	public boolean updateCellValue();

	/**
	 * Transforms the current cellValue into a displayable representation and sets it using {@link #setDisplayValue(Object)}
	 */
	public void updateDisplayValue();

	
	boolean isEditable();

	void setEditable(boolean b);
	
	Object getBackground();
	void setBackground(Object nativeColor);
	
	Object getForeground();
	void setForeground(Object nativeColor);

	boolean hasValidCellValue();

	String getValidationFailureMessage();
	
	
	/**
	 * @return never null
	 */
	Object getImplementation();
	
	
	interface Delegate {
		/**
		 * The cell rejected the given cell-value e.g. because it does not meet the required format.
		 * But since there might be meta-values like the Selection-Markers, that should be displayed anyway,
		 * you can return true here:
		 * @param sender TODO
		 * @param invalidValue
		 * @param errMsg
		 * @return
		 */
		boolean cellShouldAcceptInvalidValue(NSCell sender, Object invalidValue, String errMsg);
		
		/**
		 * The cell rejected the given input/display value e.g. because it does not meet the required format.
		 * But since there might be meta-values like the Selection-Markers, that should be displayed anyway,
		 * you can return true here.
		 * @param sender TODO
		 * @param invalidValue
		 * @param errMsg
		 * 
		 * @return
		 */
		boolean cellShouldAcceptInvalidPartialValue(NSCell sender, Object invalidValue, String errMsg);
		
		/**
		 * Invoked from the cell, if there had been changes done via the gui.
		 * @param sender
		 */
		void cellValueDidChangeViaGUI(NSCell sender);
		
		Object cellValidateCellValue(NSCell sender, Object c) throws ValidationException;
		

		Object cellValidateDisplayValue(NSCell sender, Object obj) throws ValidationException;
		
		
		class Dummy implements Delegate {
			@Override
			public void cellValueDidChangeViaGUI(NSCell sender) {
			}
			
			@Override
			public Object cellValidateDisplayValue(NSCell sender, Object obj)
					throws ValidationException {
				return obj;
			}
			
			@Override
			public Object cellValidateCellValue(NSCell sender, Object c) throws ValidationException {
				return c;
			}

			@Override
			public boolean cellShouldAcceptInvalidValue(NSCell sender,
					Object invalidValue, String errMsg) {
				return false;
			}
			

			@Override
			public boolean cellShouldAcceptInvalidPartialValue(NSCell sender, Object invalidValue, String errMsg) {
				return true;
			}


			@Override
			public boolean hasValidCellValue(boolean cellState) {
				return cellState;
			}
			
			public final static Delegate INSTANCE = new Dummy();
		}


		/**
		 * 
		 * @param cellState true means cell thinks its ok
		 * @return
		 */
		boolean hasValidCellValue(boolean cellState);




	}

	/**
	 * e.g. paint it red or beep. Maybe show message
	 * 
	 * @param m
	 */
	@Deprecated
	public void notifyMessage(String m);
	

	/**
	 * Check if <code>newCellVal</code> can be used as this cell's cell-Value.
	 * 
	 * This method should not change this cell's state.
	 * @param newCellVal
	 * @return newCellVal or a value that should be used instead of <code>newCellVal</code>.
	 * @throws ValidationException if <code>newCellVal</code> is not acceptable and a replacement could not be found.
	 */
	Object validateCellValue(Object newCellVal) throws ValidationException;


	
	/**
	 * Experimental!?
	 * @return
	 */
	boolean hasFocus();
	
	void requestFocus();
	
	
//	/**
//	 * Informs the cell, that it's value is to be treated as a special marker value i.e. an NSSellectionMarker or its placeholder.  
//	 * @param isMarkerState
//	 */
//	void setMarkerState(boolean isMarkerState);
//	boolean isMarkerState();
//	
	
	
//	void setMarkerValue(Object markerOrPlaceholder);

}