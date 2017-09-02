package de.ceruti.curcuma.api.appkit;



public interface NSEditable {

	/**
	 * invoked to inform the receiver that editor has uncommitted changes that can affect the receiver.
	 */
	void objectDidBeginEditing(NSEditor editor);

	/**
	 * Invoked to inform the receiver that editor has committed or discarded its changes.
	 */
	void objectDidEndEditing(NSEditor editor);

	/**
	 * Returns YES if there are any editors currently registered with the receiver, NO otherwise.
	 * @return
	 */
	boolean hasEditors();

	/**
	 * Discards any pending changes by registered editors.
	 * 
	 * The receiver invokes discardEditing on any current editors.
	 */
	void discardEditing(Object sender);
	
	/**
	 * Causes the receiver to attempt to commit any pending edits, returning YES
	 * if successful or no edits were pending.
	 * 
	 * 
	 * The receiver invokes commitEditing on any current editors, returning
	 * their response. A commit is denied if the receiver fails to apply the
	 * changes to the model object, perhaps due to a validation error.
	 * 
	 * @return
	 */
	boolean commitEditing(Object sender);
}