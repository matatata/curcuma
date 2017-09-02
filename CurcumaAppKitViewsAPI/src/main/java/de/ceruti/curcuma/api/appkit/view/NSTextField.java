package de.ceruti.curcuma.api.appkit.view;


public interface NSTextField extends NSEditorControl {
	String TextValueBinding = "text";
	
//	/**
//	 * @see #setContinuousCommit(boolean)
//	 * @return
//	 */
//	boolean isContinuousCommit();
//	
//	/**
//	 * Control whether a commit will be issued as you type
//	 * @param b
//	 */
//	void setContinuousCommit(boolean c);
//	
	/**
	 * @see #setAutoCommit(boolean)
	 * @return
	 */
	boolean isAutoCommit();
	
	/**
	 * Control whether a commit will be issued when loosing focus
	 * @param b
	 */
	void setAutoCommit(boolean b);
	
	
	void setText(String t);
	String getText();
}
