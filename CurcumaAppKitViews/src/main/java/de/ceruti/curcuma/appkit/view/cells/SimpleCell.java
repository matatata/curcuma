package de.ceruti.curcuma.appkit.view.cells;

import de.ceruti.curcuma.api.appkit.NSEditor;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;

public class SimpleCell extends AbstractCellBase implements NSEditorCell {

	@Override
	public void cancelCellEditing() {
	}

	@Override
	public boolean stopCellEditing() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell#editorDidEndEditing
	 * (de.ceruti.curcuma.api.appkit.NSEditor)
	 */
	@Override
	public void editorDidEndEditing(NSEditor editor) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell#editorDidStartEditing
	 * (de.ceruti.curcuma.api.appkit.NSEditor)
	 */
	@Override
	public void editorDidStartEditing(NSEditor editor) {
	}
}
