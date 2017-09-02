package de.ceruti.curcuma.api.appkit.view.table;

import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;

public interface NSTableColumn extends KeyValueCoding, KeyValueObserving,
		KeyValueBindingCreation {
	
	NSTableView getTableView();

	void setTableView(NSTableView v);

	NSCell getDataCell();

	void setDataCell(NSCell c);

	NSCell getHeaderCell();

	void setHeaderCell(NSCell c);

	NSEditorCell getEditorCell();

	void setEditorCell(NSEditorCell c);

	Object getIdentifier();

	void setIdentifier(Object o);
	
//	/**
//	 * @deprecated
//	 * If {@link #ValueBinding} is bound, then you should be able to get the value at the given row.
//	 * @param row
//	 * @return
//	 */
//	Object getBoundObjectAtRow(int row);
//	
//	/**
//	 * @deprecated
//	 * @see #getBoundObjectAtRow(int)
//	 * @param o
//	 * @param row
//	 */
//	void setBoundObjectAtRow(Object o, int row);

	TableColumnDataSource getContentValuesDataSource();
	void setContentValuesDataSource(TableColumnDataSource s);
	TableColumnDataSource getContentDataSource();
	void setContentDataSource(TableColumnDataSource s);
}
