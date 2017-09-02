package de.ceruti.curcuma.appkit.view.cells;

import de.ceruti.curcuma.api.appkit.NSEditor;
import de.ceruti.curcuma.api.appkit.view.cells.NSToggleCell;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

public class NSToggleCellImpl extends NSActionCellImpl implements NSToggleCell {

	public NSToggleCellImpl() {
		exposeBinding(TitleBinding,String.class);
	}
	
	@Override
	public ToggleWidgetPlugin getWidgetPlugIn(){
		return (ToggleWidgetPlugin)super.getWidgetPlugIn();
	}
	
	@Override
	@PostKVONotifications
	public void setSelected(boolean b) {
		getWidgetPlugIn().setSelected(b);
	}
	
	@Override
	public boolean isSelected() {
		return getWidgetPlugIn().isSelected();
	}
	
	@Override
	@PostKVONotifications
	public void setTitle(String t) {
		getWidgetPlugIn().setTitle(t);
	}
	
	@Override
	public String getTitle() {
		return getWidgetPlugIn().getTitle();
	}
	
	@Override
	public final void cancelCellEditing() {
		updateDisplayValue();
	}

	@Override
	public final boolean stopCellEditing() {
		return updateCellValue();
	}
	
	@Override
	public void editorDidEndEditing(NSEditor editor) {
	}

	@Override
	public void editorDidStartEditing(NSEditor editor) {
	}
}
