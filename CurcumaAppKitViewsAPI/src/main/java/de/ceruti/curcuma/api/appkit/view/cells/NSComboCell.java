package de.ceruti.curcuma.api.appkit.view.cells;

import java.util.List;

import de.ceruti.curcuma.api.appkit.view.ListDataSource;
import de.ceruti.curcuma.api.core.IndexSet;

public interface NSComboCell extends NSEditorCell {
	String ContentBinding = "content";
	String ContentValuesBinding = "contentValues";
	String CellValueIndexBinding ="cellValueIndex";
	
	Delegate getComboDelegate();
	
	ListDataSource getDisplayDataSource();
	void setDisplayDataSource(ListDataSource displayDataSource);

	ListDataSource getContentDataSource();
	void setContentDataSource(ListDataSource contentDataSource);
	
	void setContentDataArray(List elements);
	void setDisplayDataArray(List elements);

	void listReloadData();

	void listContentsChanged(IndexSet indexes);

	void listIntervalAdded(IndexSet indexes);

	void listIntervalRemoved(IndexSet indexes);
	
	int getCellValueIndex();
	void setCellValueIndex(int s);
	

	interface Delegate extends NSEditorCell.Delegate, NSActionCell.Delegate {
		class Dummy extends NSEditorCell.Delegate.Dummy implements Delegate {
			public static final Delegate INSTANCE=new Delegate.Dummy();

			@Override
			public void cellDidPerformAction(NSActionCell sender) {
			}

			@Override
			public boolean cellShouldPerformAction(NSActionCell sender) {
				return true;
			}
		}
	}
}
