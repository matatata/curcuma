package de.ceruti.curcuma.appkit.view.table;

import java.util.List;

import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.appkit.view.table.TableDataSource;


public abstract class AbstractTableDataSource implements TableDataSource {

	protected abstract List<?> contentArray();
	
	@Override
	public int getColumnCount(NSTableView sender) {
		return sender.getTableColumns().size();
	}

	@Override
	public int getRowCount(NSTableView sender) {
		return contentArray().size();
	}

	/**
	 * @return null
	 */
	public Class<?> getColumnClass(NSTableColumn col) {
		return null;
	}

	public abstract boolean isCellEditable(int row, NSTableColumn col);
}