package de.ceruti.curcuma.api.appkit.view.cells;

import de.ceruti.curcuma.api.appkit.NSEditor;

public class NullNSEditorCell extends NullNSCell implements NSEditorCell {

	protected NullNSEditorCell(){}
	
	private static NSEditorCell instance = new NullNSEditorCell();
	
	public static NSEditorCell instance() {
		return instance;
	}
	
	@Override
	public void cancelCellEditing() {

	}

	@Override
	public void editorDidEndEditing(NSEditor editor) {

	}

	@Override
	public void editorDidStartEditing(NSEditor editor) {

	}


	@Override
	public de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell.Delegate getEditorDelegate() {
		return null;
	}


	@Override
	public boolean stopCellEditing() {
		return false;
	}

}
