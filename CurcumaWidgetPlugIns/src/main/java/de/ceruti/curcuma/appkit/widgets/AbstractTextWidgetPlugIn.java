package de.ceruti.curcuma.appkit.widgets;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.NSText;
import de.ceruti.curcuma.appkit.view.cells.EditorWidgetPlugin;
import de.ceruti.curcuma.appkit.view.cells.TextFieldCellWidgetPlugin;

public abstract class AbstractTextWidgetPlugIn extends AbstractActionWidgetPlugIn
		implements TextFieldCellWidgetPlugin {

	private static Category logger = Logger
			.getInstance(AbstractTextWidgetPlugIn.class);

	protected boolean inEditingState = false;

	protected AbstractTextWidgetPlugIn() {
		super();
	}

	/**
	 * @return {@link #getText()}
	 */
	@Override
	public Object getPlugInValue() {
		return getText();
	}

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.api.appkit.view.NSText#getText()
	 */
	public final String getText() {
		return getString();
	}

	
	@Override
	public TextFieldCellWidgetPlugin.Delegate getWidgetAssociation() {
		Object assoc = super.getWidgetAssociation();
		if (assoc instanceof TextFieldCellWidgetPlugin.Delegate)
			return (TextFieldCellWidgetPlugin.Delegate) assoc;
		return TextFieldCellWidgetPlugin.Delegate.Dummy.INSTANCE;
	}

	
	protected final boolean isEditingState() {
		return inEditingState;
	}

	protected final void setEditingState(boolean editing) {
		this.inEditingState = editing;
	}

	
	@Override
	protected void _setPlugInValue(Object p) {
		setText(p == null ? "" : p.toString());
	}

	/**
	 * Sets the plugin's text using {@link #setString(String)}. Might notify the widget association.
	 * @param t the new Text to set
	 * @see de.ceruti.curcuma.api.appkit.view.NSText#setText(java.lang.String)
	 * @see #setString(String)
	 * @see #notifyAssociationEnabled()
	 */
	public final void setText(String t) {
		String orig = getString();
		if (orig.equals(t))
			return;

		setString(t);
	}

//	public void textBeginEditing() {
//
//		logger.debug("textEndEditing()");
//		setEditingState(true);
//		requestFocus();
//	}

	
	private Object origBGColor;
	
	private Object origFGColor;
	
	private boolean validityState = true;
	public void setValidityStatus(boolean isValid, String reason) {
		
		if(this.validityState == isValid)
			return;
		
		this.validityState = isValid;
		
		if(!isValid) {
			if(origFGColor==null){
				origFGColor = getForeground();
			}
			setForeground(getWidgetAssociation().getInvalidForeground());
		}
		else if(origFGColor!=null){
			setForeground(origFGColor);
		}
	}
	
	public void textBeginEditing() {
		logger.debug("textBeginEditing()");
		setEditingState(true);
		textRequestFocus();
		
		if(getWidgetAssociation().getEditBackground()==null)
			return;
		
		if (origBGColor == null)
			origBGColor = getBackground();
		setBackground(getWidgetAssociation().getEditBackground());
	}


	
	/**
	 * Should be called, whenever the Text has changed.
	 * Does nothing, if isIgnoreTextEvents returns true.
	 * NSText.Delegate.textDidChange(this) of the Association will always be
	 * invoked
	 */
	protected void textHasChanged() {
		if (notifyAssociationEnabled()) {
//			logger.debug("textHasChanged()");
			if (!isEditingState()) {
				textBeginEditing();
				getWidgetAssociation().textDidBeginEditing(this);
				getWidgetAssociation().plugInDidBeginEditing(this);
			}
//			setDirty(true);
			if(getWidgetAssociation().plugInValueDidChangeViaGui(this)){
//				setDirty(false);
			}
			
		} else {
//			logger.debug("textEditedViaGui() ignoring change");
		}

		logger.debug("textEditedViaGui() posting textDidChange");
		getWidgetAssociation().textDidChange(this);
	}

	public void textEndEditing() {
		logger.debug("textEndEditing()");
		setEditingState(false);
		
		if (origBGColor != null)
			setBackground(origBGColor);
	
	}

	/**
	 * calls {@link #stopListeningForTextEvents()}
	 * {@link #stopListeningForFocusEvents()}
	 * {@link #stopListeningForActionEvents()} and the inherited implementation
	 */
	@Override
	protected void breakCellConnection() {
		stopListeningForActionEvents();
		stopListeningForFocusEvents();
		stopListeningForTextEvents();
		stopListeningForKeyEvents();
		super.breakCellConnection();
	}

	/**
	 * calls the inherited implementation and
	 * {@link #startListeningForTextEvents()}
	 * {@link #startListeningForActionEvents()}
	 * {@link #startListeningForFocusEvents()}
	 */
	@Override
	protected void establishCellConnection() {
		super.establishCellConnection();
		startListeningForTextEvents();
		startListeningForActionEvents();
		startListeningForFocusEvents();
		startListeningForKeyEvents();
	}

	protected void focusGained() {
		logger.debug("focus gained");
		getWidgetAssociation().textReceivedFocus(AbstractTextWidgetPlugIn.this);
	}

	protected void focusLost() {
		logger.debug("focus lost");
		
		if (getWidgetAssociation().textShouldEndEditing(
				AbstractTextWidgetPlugIn.this)) {
			textEndEditing();
			getWidgetAssociation().textDidEndEditing(
					AbstractTextWidgetPlugIn.this);
		}
	}
	
	private long ESC_INTERVAL=500;
	private long lastTime=Long.MIN_VALUE;
			
	public void cancelKeyTyped() {
		long delta =System.currentTimeMillis() - lastTime;
		if(delta < ESC_INTERVAL && delta > 0)
			getWidgetAssociation().plugInDidCancelEditing(this);
		else 
			lastTime=System.currentTimeMillis();
		
	};
	
	@Override
	protected void actionPerformed() {
		textHasChanged();
		super.actionPerformed();
	}

	protected abstract String getString();

	protected abstract void setString(String s);

	/**
	 * Called on establishConnection
	 */
	protected abstract void startListeningForActionEvents();

	/**
	 * Called on establishConnection
	 */
	protected abstract void startListeningForFocusEvents();

	/**
	 * Called on establishConnection
	 */
	protected abstract void startListeningForTextEvents();

	/**
	 * Called on breakConnection
	 */
	protected abstract void stopListeningForActionEvents();

	/**
	 * Called on breakConnection
	 */
	protected abstract void stopListeningForFocusEvents();

	/**
	 * Called on breakConnection
	 */
	protected abstract void stopListeningForTextEvents();
	
	/**
	 * Called on establishConnection
	 */
	protected abstract void startListeningForKeyEvents();

	/**
	 * Called on breakConnection
	 */
	protected abstract void stopListeningForKeyEvents();

}
