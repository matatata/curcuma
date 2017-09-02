package de.ceruti.curcuma.api.appkit;

import de.ceruti.curcuma.api.core.utils.Observer;
import de.ceruti.curcuma.api.core.utils.Subject;


public interface NSEditor extends Subject<Observer<NSEditorEvent>, NSEditorEvent>{

	/**
	 * 
	 * @return true if a commit is in progress
	 */
	boolean isCommitting();
	
	
	/**
	 * 
	 */
	void startEditing();
	
	void endEditing();
	
	/**
	 * Causes the receiver to discard any changes, restoring the previous
	 * values.
	 */
	void discardEditing(Object sender);
	
	/**
	 * Returns whether the receiver was able to commit any pending edits.
	 * Returns YES if the changes were successfully applied to the model, NO
	 * otherwise. A commit is denied if the receiver fails to apply the changes
	 * to the model object, perhaps due to a validation error.
	 * 
	 * endEditing() will be called on success
	 */
	boolean commitEditing(Object sender);
	
//	interface NSCommitCallback {
//		/**
//		 * if an error occurs while attempting to commit, for example if
//		 * key-value coding validation fails, an implementation of this method
//		 * should typically send the NSView in which editing is being done a
//		 * presentError:modalForWindow:delegate:didRecoverSelector:contextInfo:
//		 * message, specifying the view's containing window.
//		 * 
//		 * @param editor
//		 * @param didCommit the committing has either succeeded or failed
//		 * @param context
//		 */
//		void editorDidCommit(NSEditor editor,boolean didCommit,Object context);
//	}
//	
//	/**
//	 * The receiver must have been registered as the editor of an object using
//	 * objectDidBeginEditing:, and has not yet been unregistered by a subsequent
//	 * invocation of objectDidEndEditing:. When the committing has either
//	 * succeeded or failed, send the following message to the specified object.
//	 * The didCommitSelector method must have the following method signature:
//	 * 
//	 * void editorDidCommit(NSEditor editor,boolean didCommit,Object context);
//	 * @param c
//	 * @param context
//	 * @return false if committing failed due to an error
//	 */
//	boolean commitEditing(NSCommitCallback c,Object context);
	
	
	void setAutomaticCommit(boolean b);
	
	boolean isAutomaticCommit();
	
	void setAutomaticValidation(boolean b);
	
	boolean isAutomaticValidation();
	
	void setEditingSubject(NSEditable owner);

	NSEditable getEditingSubject();

	boolean isEditing();
	
	void notifyValidationStatus(boolean success,String message);
}
