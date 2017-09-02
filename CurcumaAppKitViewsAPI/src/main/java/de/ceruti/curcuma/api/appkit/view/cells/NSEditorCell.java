package de.ceruti.curcuma.api.appkit.view.cells;

import de.ceruti.curcuma.api.appkit.NSEditor;

public interface NSEditorCell extends NSCell {
	/**
	 * Tells the editor to stop editing and accept any partially edited value as
	 * the value of the editor. The editor returns false if editing was not
	 * stopped; this is useful for editors that validate and can not accept
	 * invalid entries.
	 */
	boolean stopCellEditing();

	/**
	 * Tells the editor to cancel editing and not accept any partially edited
	 * value.
	 */
	void cancelCellEditing();
	
	
	Delegate getEditorDelegate();
	
	//Tell the cell that there are uncomitted changes
	void editorDidStartEditing(NSEditor editor);
	//Tell the cell that there are no uncommited changes
	void editorDidEndEditing(NSEditor editor);
	

	interface Delegate extends NSCell.Delegate {
		// Tell the delegate, that
		void cellDidBeginEditing(NSEditorCell sender);

		void cellDidCancelEditing(NSEditorCell sender);

		class Dummy extends NSCell.Delegate.Dummy implements
				Delegate {
			@Override
			public void cellDidBeginEditing(NSEditorCell sender) {
			}

			@Override
			public void cellDidCancelEditing(NSEditorCell sender) {
			}

			public static final Delegate INSTANCE = new Delegate.Dummy();

		}
	}
}
