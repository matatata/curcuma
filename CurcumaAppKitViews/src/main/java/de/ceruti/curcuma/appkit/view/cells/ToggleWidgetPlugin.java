package de.ceruti.curcuma.appkit.view.cells;


public interface ToggleWidgetPlugin extends ActionControlWidgetPlugin {

	boolean isSelected();
	void setSelected(boolean s);
	
	
	void setTitle(String t);
	String getTitle();
	

}
