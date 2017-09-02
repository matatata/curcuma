package de.ceruti.curcuma.api.appkit.view.cells;


public interface NSToggleCell extends NSActionCell, NSEditorCell {
	
	String TitleBinding="title";
	
	boolean isSelected();
	void setSelected(boolean b);
	
	void setTitle(String t);
	String getTitle();
}
