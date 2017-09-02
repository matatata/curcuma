package de.ceruti.curcuma.api.appkit.view.table;

import java.util.List;

import de.ceruti.curcuma.api.appkit.NSEditable;
import de.ceruti.curcuma.api.appkit.view.NSView;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.core.IndexSet;

/**
 * 
 * @author cerutim
 * TODO: should also implement NSEditor!
 */
public interface NSTableView extends NSView {

	String ContentArrayBinding = "contentArray";
	String SelectionIndexesBinding = "selectionIndexes";

	void setSelectionIndexes(IndexSet indexes);

	IndexSet getSelectionIndexes();

	void addTableColumn(NSTableColumn col);

	boolean removeTableColumn(NSTableColumn col);

	int indexOfColumnWithIdentifier(Object identifier);

	int indexOfColumn(NSTableColumn col);
	
	int getTableColumnCount();

	NSTableColumn tableColumnWithIdentifier(Object identifier);

	NSTableColumn getTableColumAtIndex(int columnIndex);

	List<NSTableColumn> getTableColumns();

	TableDataSource getDataSource();

	void setDataSource(TableDataSource s);

	// The Widget should update its view
	void tableDataChanged(); // refresh all

	void tableStructureChanged();
	
	void tableRowsUpdated(IndexSet rows);

	void tableRowsInserted(IndexSet rows);

	void tableRowsDeleted(IndexSet rows);

	void tableCellUpdated(IndexSet rows, int col);

	NSCell preparedCell(Object contentValue, boolean isSelected, boolean hasFocus,
			int row, int col);

	NSEditorCell preparedEditorCell(Object contentValue, boolean isSelected, int row, int col);

	NSEditable getEditingSubject();
	void setEditingSubject(NSEditable editingSubject);
	
	Delegate getDelegate();
	void setDelegate(Delegate d);
	
	
	NSEditorCell getDefaultEditor(Class<?> clazz);
	void setDefaultEditor(Class<?> clazz,NSEditorCell cell);
	
	public static interface Delegate {
		boolean isCellEditable(int row,NSTableColumn col);

		boolean shouldChangeSelectionIndexes(IndexSet newSelection);
		
		public static class Dummy implements Delegate {

			public static final Delegate INSTANCE = new Dummy();
			
			@Override
			public boolean isCellEditable(int row, NSTableColumn col) {
				return true;
			}
			

			@Override
			public boolean shouldChangeSelectionIndexes(IndexSet newSelection) {
				return true;
			}

			@Override
			public NSEditorCell getEditorCell(int row, int col) {
				return null;
			}
		}

		NSEditorCell getEditorCell(int row, int col);
		
	}
}