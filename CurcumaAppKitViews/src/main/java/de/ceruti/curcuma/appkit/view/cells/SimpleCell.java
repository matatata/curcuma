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

	@Override
	public void editorDidEndEditing(NSEditor editor) {

	}

	@Override
	public void editorDidStartEditing(NSEditor editor) {
	}
}
