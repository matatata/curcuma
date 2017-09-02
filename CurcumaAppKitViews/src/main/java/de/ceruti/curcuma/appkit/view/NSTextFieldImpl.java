package de.ceruti.curcuma.appkit.view;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSSelectionMarker;
import de.ceruti.curcuma.api.appkit.view.NSText;
import de.ceruti.curcuma.api.appkit.view.NSTextField;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSTextCell;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;

public class NSTextFieldImpl extends NSEditingControl implements NSTextField {

	private static Category logger = Logger.getInstance(NSTextFieldImpl.class);
	
	
	public NSTextFieldImpl() {
		super();
		exposeBinding(TextValueBinding,String.class);
//		addObserver(new Observer<NSEditorEvent>() {
//			public void handleEvent(NSEditorEvent e) {
//				if(e.getType()==NSEditorEvent.Type.DISCARD){
//					if(getCell() instanceof NSText)
//						clearTextIfMarker((NSText) getCell());
//				}
//			}
//		});
	}
	
	@Override
	protected void onDiscard(Object sender){
		if(sender instanceof NSText)
			clearTextIfMarkerAndOrSelectAll((NSText) sender);
	}
	
	/**
	 * TODO use cell not sender
	 * @param sender
	 */
	private void clearTextIfMarkerAndOrSelectAll(NSText sender) {
		// Experimental!
		if (((NSCell)sender).hasFocus() &&
				!isEditing() &&
				isMarkerState() != null
				&& (isMarkerState().equals(NSSelectionMarker.NSMultipleValuesMarker)
						|| isMarkerState().equals(NSSelectionMarker.NSNullValueMarker))) {
			((NSCell)sender).setDisplayValue("");
		}
		else
			sender.selectAll();
		

	}
	
	private boolean autoCommit = true;
	@Override
	public boolean isAutoCommit() {
		return autoCommit;
	}
	
	@Override
	public void setAutoCommit(boolean b){
		this.autoCommit = b;
	}
	
	protected class NSCellDelegate extends NSEditingControl.NSCellDelegate
			implements NSTextCell.Delegate {
		public NSCellDelegate() {
		}

//		@Override
//		public boolean cellValueDidChange(NSCell sender) {
//			boolean ret = super.cellValueDidChange(sender);
//			if(isContinuousCommit()){
//				boolean ret2 = commitEditing();
//			}
//			//TODO what Todo about ret2?
//			return ret /* && ret2*/;
//		}

		@Override
		public void textDidChange(NSText sender) {

			String newTxt = sender.getText();
			if(ObjectUtils.equals(NSTextFieldImpl.this.text, newTxt))
				return;
			
			willChangeValueForKey("text");
			logger.debug("text changed from '" + NSTextFieldImpl.this.text + "' to '" + newTxt + "'");
			NSTextFieldImpl.this.text = newTxt;
			didChangeValueForKey("text");
		}
		
				
		@Override
		public void textDidBeginEditing(NSText editor) {
			logger.debug("textDidBeginEditing()");
//			startEditing();
			//will be called by NSEditingControl.NSCellDelegate.cellDidBeginEditing
			//
			
		}
		
		@Override
		public void textDidEndEditing(NSText editor) {
			logger.debug("textDidEndEditing()");
			if (isAutoCommit() && isEditing())
				commitEditing(editor);
			else if (isMarkerState() != null) {
				updateCellValue();
			}
		}
		
		
		@Override
		public void textReceivedFocus(NSText sender) {
			logger.debug("textReceivedFocus()");
			clearTextIfMarkerAndOrSelectAll(sender);
		}
		
		@Override
		public void cellDidCancelEditing(NSEditorCell sender) {
			super.cellDidCancelEditing(sender);
			if(sender instanceof NSText)
				clearTextIfMarkerAndOrSelectAll((NSText) sender);
		}
		
		@Override
		public boolean textShouldBeginEditing(NSText editor) {
			return true;
		}

		
//		private boolean isNotifying = false;
		
		@Override
		public boolean textShouldEndEditing(NSText editor) {
			if(!isEditing())
				return true;
			
			return checkValidValuesAndNotify((NSCell)editor);
		}

	}
	
	@Override
	protected NSCell.Delegate createDefaultCellDelegate() {
		return new NSCellDelegate(); 
	}
	
	
	private String text;
	
	/**
	 * 
	 * @param s
	 */
	@Override
	public void setText(String s) {
		try {
			willChangeValueForKey("text");
			this.text = s;
			if(getCell() instanceof NSText)
				((NSText)getCell()).setText(s);
		} 
		finally {
			didChangeValueForKey("text");
		}
	}
	
	@Override
	public String getText() {
		return text;
	}

//	private boolean continuousCommit = false;
//	
//	public boolean isContinuousCommit() {
//		return continuousCommit;
//	}
//	
//	public void setContinuousCommit(boolean c) {
//		continuousCommit = c;
//	}
	

	
	//NSEditorAspect implementation:

	@Override
	public boolean onCommit(StringBuffer err) {
		boolean ret = super.onCommit(err);
		
		try {
			updateObservableBoundToBinding(TextValueBinding);
			return ret;
		} catch (KeyValueBindingSyncException e) {
			logger.error(e);
			handleKVBSyncException(e, err);
			return false;
		} finally {
			if(ret) {
				if (getCell() instanceof NSText)
					((NSText) getCell()).textEndEditing();
			}
		}
	}
	
	@Override
	public boolean revert() {
		boolean ret = super.revert();
		try {
			updateBinding(TextValueBinding);
		} catch (KeyValueBindingSyncException e) {
			logger.error(e);
			return false;
		} finally {
			if (getCell() instanceof NSText)
				((NSText) getCell()).textEndEditing();
		}
		
		return ret;
	}

	
}
