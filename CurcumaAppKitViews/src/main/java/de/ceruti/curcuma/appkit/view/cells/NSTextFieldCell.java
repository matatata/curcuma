package de.ceruti.curcuma.appkit.view.cells;

import java.text.Format;
import java.text.ParseException;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditor;
import de.ceruti.curcuma.api.appkit.view.NSText;
import de.ceruti.curcuma.api.appkit.view.cells.NSTextCell;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;


public final class NSTextFieldCell extends NSActionCellImpl implements TextFieldCellWidgetPlugin.Delegate, NSTextCell {
	private static transient Category logger = Logger.getInstance(NSTextFieldCell.class);

	private Format format;
	private Format formatHasFocus;

	
	public NSTextFieldCell() {
		super();
	}
	
	@Override
	public final Format getFormat() {
		return format;
	}

	@Override
	public final void setFormat(Format format) {
		this.format = format;
	}

	@Override
	public final void setFormatWhenFocused(Format format) {
	    this.formatHasFocus = format;
	}
	
	@Override
	public final Format getFormatWhenFocused() {
	  return this.formatHasFocus;
	}
	
	private Format _getCurrentFormat() {
	  if(hasFocus && getFormatWhenFocused()!=null)
	    return getFormatWhenFocused();
	  return getFormat();
	}
	
	private boolean needsPlugInValueUpdateOnFocusChange(){
		if(getFormat()!=getFormatWhenFocused())
			return true;
		if(getFormat()!=null && !getFormat().equals(getFormatWhenFocused()))
			return true;
		if(getFormatWhenFocused()!=null && !getFormatWhenFocused().equals(getFormat()))
			return true;
		
		return false;
	}
	
//	private boolean nseditorIsEditing = false;
//	
//	protected boolean isEditing() {
//		return nseditorIsEditing;
//	}
	
	
	Object origBGColor;
	@Override
	public void editorDidEndEditing(NSEditor editor) {
//		nseditorIsEditing = false;
		
		if(origBGColor!=null)
		{
			getTextWidgetPlugIn().setBackground(origBGColor);
		}
		
	}

	@Override
	public void editorDidStartEditing(NSEditor editor) {
		if(getUncommitedChangeBackground()!=null){
			origBGColor = getTextWidgetPlugIn().getBackground();
			getTextWidgetPlugIn().setBackground(getUncommitedChangeBackground());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSText#textBeginEditing()
	 */
	@Override
	public final void textBeginEditing(){
		getTextWidgetPlugIn().textBeginEditing();
	}
	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSText#textEndEditing()
	 */
	@Override
	public final void textEndEditing(){
		getTextWidgetPlugIn().textEndEditing();
	}

	@Override
	public NSTextCell.Delegate getTextDelegate() {
		Object del = super.getDelegate();
		if(del!=null && del instanceof NSTextCell.Delegate)
			return (NSTextCell.Delegate)del;
		return NSTextCell.Delegate.Dummy.INSTANCE;
	}
	
	

	
	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.appkit.view.EditorWidgetPlugin.Delegate#plugInDidBeginEditing(de.ceruti.curcuma.appkit.view.EditorWidgetPlugin)
	 */
	@Override
	public final void plugInDidBeginEditing(EditorWidgetPlugin sender) {
		getEditorDelegate().cellDidBeginEditing(this);
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSText.Delegate#textDidBeginEditing(de.ceruti.curcuma.api.appkit.view.NSText)
	 */
	@Override
	public final void textDidBeginEditing(NSText editor) {
		// hasFocus = true;
		getTextDelegate().textDidBeginEditing(this);
	}

	
	private boolean hasFocus = false;
	
	@Override
	public final void textDidEndEditing(NSText editor) {

		hasFocus = false;
		if (needsPlugInValueUpdateOnFocusChange()) {
			updateDisplayValue();
			
		}
    
		getTextDelegate().textDidEndEditing(this);
	}
	

	@Override
	public final void textReceivedFocus(NSText editor) {
		hasFocus = true;
		if(needsPlugInValueUpdateOnFocusChange()){
			updateDisplayValue();
		}

		
		getTextDelegate().textReceivedFocus(this);
	}
	
	@Override
	public void textRequestFocus() {
		requestFocus();
	}
  
	@Override
	public final boolean textShouldBeginEditing(NSText editor) {
		return getTextDelegate().textShouldBeginEditing(this);
	}

	@Override
	public final boolean textShouldEndEditing(NSText editor) {
		return getTextDelegate().textShouldEndEditing(this);
	}

	@Override
	public final void textDidChange(NSText sender) {
		getTextDelegate().textDidChange(this);
	}
	
	@Override
	public final Object validateDisplayValue(Object obj) throws ValidationException {
		String s = obj != null ? obj.toString() : null;
		obj = format(parseString(s));
		return super.validateDisplayValue(obj);
	}

	@Override
	public
	final Object validateCellValue(Object value)
			throws ValidationException {
		
		if (_getCurrentFormat() == null)
			return super.validateCellValue(value);

		try{
			//this is used to check if format can be applied..
			//if not the cell value is not valid!
			format(value);
		}catch(ValidationException e){
			throw e;
		}
		return super.validateCellValue(value);
	}
	
	@Override
	protected final Object cell2DisplayValue(Object c) throws ValidationException {
		if (_getCurrentFormat() == null) {
			return super.cell2DisplayValue(c);
		}
		
		try {
			c = format(c);
		} catch (ValidationException e) {
			if(hasValidCellValue())
				throw e;
			
			c = c == null ? null : c.toString();
		}

		return c;
	}
	
	@Override
	protected final Object display2cellValue(Object d) throws ValidationException {
		return parseString(d == null ? null : d.toString());
	}

	/**
	 * If there is no formatter the method will return
	 * <code>obj.toString()</code> or null if <code>obj==null</code>. If there
	 * is a formatter, <code>obj</code> will be formatted.
	 * @param obj
	 * @throws ValidationException
	 *             if formatting failed for <code>obj</code>
	 * @return If there is no formatter this will return
	 *         <code>obj.toString()</code> or null if <code>obj==null</code>.
	 * @see Format
	 */
	private String format(Object obj) throws ValidationException{
		
		Format fmt = _getCurrentFormat();
		
		if(fmt==null){
			if(obj==null)
				return null;
			return obj.toString();
		}
		
		try {
			String txt = fmt.format(obj);
			return txt;
		} catch(IllegalArgumentException e){
			logger.debug("obj=" + obj + " error: " + e.getMessage());
			throw new ValidationException(e);
		}
	}
	
	private Object parseString(String s) throws ValidationException {
		if( _getCurrentFormat()==null)
			return s;
		try {
			Object parsedObject = _getCurrentFormat().parseObject(s==null?"":s);
			return parsedObject;
		} catch (ParseException e) {
			throw new ValidationException(e);
		}
	}
	
	@Override
	public final void pluginDidPerformAction(ActionControlWidgetPlugin sender) {
		super.pluginDidPerformAction(sender);
		formatPlugInValue();
	}
	
	@Override
	public final void plugInDidCancelEditing(EditorWidgetPlugin sender) {
		getEditorDelegate().cellDidCancelEditing(this);
	}
	
	
	/**
	 * Tries to format the current Plugin-Value, by parsing and reformatting it.
	 * If there is no Format this method will do nothing. If the value could not
	 * be parsed and/or formatted the plugin value is not altered.
	 */
	private void formatPlugInValue() {
		try {
			Object d = getDisplayValue();
			Object c = display2cellValue(d);
			d = cell2DisplayValue(c);
			setDisplayValue(d);
		} catch (ValidationException e) {
			// current edit cannot be formatted
			// that's ok
			logger.debug(e);
		}
	}
	
	@Override
	public final void setText(String t) {
		// Set the Plugin's text
		// and update the cell value
		setDisplayValue(t);
		updateCellValue();
	}
	
	@Override
	public final String getText() {
		Object d = getDisplayValue();
		return d != null ? d.toString() : null;
	}

	@Override
	public final void cancelCellEditing() {
		updateDisplayValue();
		textEndEditing();
	}

	@Override
	public final boolean stopCellEditing() {
		boolean ret = updateCellValue();
		return ret;
	}

	
	protected TextFieldCellWidgetPlugin getTextWidgetPlugIn() {
		return (TextFieldCellWidgetPlugin)super.getWidgetPlugIn();
	}
	

	@Override
	public final void selectAll() {
		getTextWidgetPlugIn().selectAll();
	}

	@Override
	public final void clearSelection() {
		getTextWidgetPlugIn().clearSelection();
	}
	

	private Object editBGColor;
	
	@Override
	public final Object getEditBackground(){
		return editBGColor;
	}
	
	@Override
	public final void setEditBackground(Object nativeColor){
		editBGColor = nativeColor;
	}
	
	private Object nseditBGColor;
	
	public final Object getUncommitedChangeBackground(){
		return nseditBGColor;
	}
	
	public final void setUncommitedChangeBackground(Object nativeColor){
		nseditBGColor = nativeColor;
	}
	
	private Object invalidForeground;


	@Override
	public Object getInvalidForeground() {
		return invalidForeground;
	}

	@Override
	public void setInvalidForeground(Object invalidForeground) {
		this.invalidForeground = invalidForeground;
	}
}
