/**
 * 
 */
package de.ceruti.curcuma.appkit.view.table;

import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.appkit.view.table.TableDataSource;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.appkit.view.NSViewBase;
import de.ceruti.curcuma.core.Factory;

public interface TablePlugin extends NSViewBase.ViewPlugIn {

	public static interface Delegate extends NSViewBase.ViewPlugIn.Delegate,
			TableDataSource
	{

		int SELECTION_INDEXES_SET = 0;
		int SELECTION_INDEXES_ADDED = 1;
		int SELECTION_INDEXES_REMOVE = 2;
		
		IndexSet getSelectionIndexes();
		
		boolean isCellEditable(int row, NSTableColumn col);

		boolean shouldChangeSelectionIndexes(TablePlugin sender,
				IndexSet newSelection);

		/**
		 * inform that selection changed.
		 * 
		 * @param sender
		 * @param newSelection
		 * @param changeType
		 * @return true if event was handled. If not the caller might try to use
		 *         SELECTION_INDEXES_SET instead of the fine grained events.
		 * @see #SELECTION_INDEXES_ADDED
		 * @see #SELECTION_INDEXES_SET
		 * @see #SELECTION_INDEXES_REMOVED
		 * 
		 */
		boolean selectionIndexesChanged(TablePlugin sender, IndexSet newSelection, int changeType);

		public static class Dummy implements Delegate {

			@Override
			public IndexSet getSelectionIndexes() {
				return Factory.emptyIndexSet();
			}
			
			@Override
			public boolean selectionIndexesChanged(TablePlugin sender,
					IndexSet newSelection, int changeType) {
				return false;
			}

			@Override
			public boolean shouldChangeSelectionIndexes(TablePlugin sender,
					IndexSet newSelection) {
				return false;
			}

			
			@Override
			public int getColumnCount(NSTableView sender) {
				return 0;
			}

			@Override
			public int getRowCount(NSTableView sender) {
				return 0;
			}

			@Override
			public boolean isCellEditable(int row, NSTableColumn col) {
				return false;
			}

			public static final Delegate INSTANCE = new Delegate.Dummy();

		}
	}

	// The Widget should update its selection. Does not fire Notifications
	void setSelectionIndexes(IndexSet indexes);

	// The Widget should update its view
	void tableDataChanged(); // refresh all

	void tableStructureChanged();

	void tableRowsInserted(int first, int last);

	/**
	 * @param first
	 * @param last
	 * @param updating true, if this is a sequence of calls and there will be another range to be deleted.
	 */
	void tableRowsDeleted(int first, int last, boolean updating);

	void tableRowsUpdated(int first, int last);

	void tableCellUpdated(int row, int col);

	NSEditorCell getDefaultEditor(Class<?> key);
	void setDefaultEditor(Class<?> clazz,NSEditorCell cell);
}