package de.ceruti.curcuma.api.appkit.view.cells;


public interface NSBoundedRangeCell extends NSCell {
	
	static final Object UNBOUNDED=new Object();
	
	/**
	 * @param resolution -1 indicates 
	 */
	void setResolution(int resolution);
	int getResolution();
	
	Object[] getCellValueRange();
	void setCellValueRange(Object fromTo[]);
	
}
