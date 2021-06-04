package de.ceruti.curcuma.appkit.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditable;
import de.ceruti.curcuma.api.appkit.NSEditorEvent;
import de.ceruti.curcuma.api.appkit.view.NSEditorControl;
import de.ceruti.curcuma.api.appkit.view.cells.NSActionCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.cells.NullNSEditorCell;
import de.ceruti.curcuma.api.core.utils.Observer;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.appkit.NSDefaultEditor;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;

@NSDefaultEditor
public class NSEditingControl extends NSControlBase implements NSEditorControl {

private static Logger logger = LogManager.getLogger(NSEditingControl.class);
	
	
	public NSEditingControl() {
		super();
		addObserver(new Observer<NSEditorEvent>() {
			@Override
			public void handleEvent(NSEditorEvent e) {
				if(e.getType() == NSEditorEvent.Type.DISCARD)
					onDiscard(e.getSender());
				else if(e.getType() == NSEditorEvent.Type.START_EDITING)
					onStart();
				else if(e.getType() == NSEditorEvent.Type.END_EDITING)
					onEnd();
			}
		});
	}
	
	protected final NSEditorCell getEditorCell() {
		NSCell cell = super.getCell();
		if(cell instanceof NSEditorCell)
			return (NSEditorCell)cell;
	
		return NullNSEditorCell.instance();
	}
	
	protected void onDiscard(Object sender){
		
	}
	
	protected void onStart() {
		getEditorCell().editorDidStartEditing(this);
	}
	
	protected void onEnd() {
		getEditorCell().editorDidEndEditing(this);
	}
	
	protected class NSCellDelegate extends NSControlBase.NSCellDelegate
			implements NSEditorCell.Delegate {
		public NSCellDelegate() {
		}
		
		@Override
		protected void performContinousCommit(NSCell sender)
				throws KeyValueBindingSyncException {
			if (isEditing())
				commitEditing(sender);
		}
		
		/**
		 * commits
		 */
		@Override
		public void cellDidPerformAction(NSActionCell sender) {
			super.cellDidPerformAction(sender);
			
			if (!isEditing()){
				startEditing();
				((NSEditorCell)sender).stopCellEditing();
			}

			if(isEditing())//stopCellEditing above might have already triggered commit (if continously committing...is this a bug?)
				commitEditing(sender);
			
		}
		@Override
		public void cellDidCancelEditing(NSEditorCell sender) {
			if(isEditing())
				discardEditing(sender);
//			else
			super.cellDidCancelEditing(sender);
			
		}

		@Override
		public void cellDidBeginEditing(NSEditorCell sender) {
			startEditing();
		}
	}
	
	@Override
	protected NSCell.Delegate createDefaultCellDelegate() {
		return new NSCellDelegate(); 
	}

	

	
	//NSEditorAspect implementation:

	
	public boolean onCommit(StringBuffer err) {
		logger.debug("onCommit()");
		boolean success = false;
		try {
			updateObservableBoundToBinding(ControlValueBinding);
			success = true;
		} catch (KeyValueBindingSyncException e) {
			handleKVBSyncException(e,err);
			success = false;
		}
		
//		This might be not complete
		if(success){
			//Assume subject accepted controlvalue so we must not fetch it from the subject, but remove the marker state
			if (isMarkerState() != null){
				setMarkerState(null);
			}
		}

		return success;
	}
	
	private boolean isRevertOnCommitFailure = false;
	

	@Override
	public final boolean isRevertOnCommitFailure() {
		return isRevertOnCommitFailure;
	}

	@Override
	public final void setRevertOnCommitFailure(boolean isRevertOnCommitFailure) {
		this.isRevertOnCommitFailure = isRevertOnCommitFailure;
	}




	public void commitFailed(Object sender) {
		logger.error("commitFailed!");
		if(isRevertOnCommitFailure())
			discardEditing(sender);
		getCell().requestFocus();
	}
	
	public boolean revert() {
		try {
			updateBinding(ControlValueBinding);
		} catch (KeyValueBindingSyncException e) {
			return false;
		}
		return true;
	}
	
	@Override
	public void notifyValidationStatus(boolean success,String message) {
		if(!success)
			getCell().notifyMessage(message);
	
		getCell().setValidityStatus(success, message);
	}
	
	
	@Override
	public void bind(String binding, Object observable, String withKeyPath,
			BindingOptions options) {
		super.bind(binding, observable, withKeyPath, options);
		if (isBound(binding)
				&& getInfoForBinding(binding).getObservedObject() instanceof NSEditable) {
			setEditingSubject((NSEditable) observable);
		}
	}
}
