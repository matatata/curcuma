/**
 * 
 */
package de.ceruti.curcuma.api.appkit.view.table;

public interface TableDataSource {
	int getRowCount(NSTableView sender);
	int getColumnCount(NSTableView sender);
}