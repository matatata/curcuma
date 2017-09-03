package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Color;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.appkit.view.cells.NSTextFieldCell;
import de.ceruti.curcuma.appkit.view.cells.SimpleCell;
import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;
import de.ceruti.curcuma.appkit.widgets.AbstractTableViewPlugIn;

public class SwingTableViewPlugIn extends AbstractTableViewPlugIn  {
	private static Category logger = Logger.getInstance(SwingTableViewPlugIn.class);
	
	private JTable table;
	private final SelectionListener selectionListener = new SelectionListener();
	private final MyTableModel tableModel = new MyTableModel();
	private TableModel oldModel;

	
	
	private class SelectionListener extends JListSelectionListener {

		@Override
		protected void selectionChanged(IndexSet newSet) {
			selectionDidChangedViaGUI(newSet);
		}

		
	}

	@SuppressWarnings("serial")
	private class MyTableModel extends AbstractTableModel {

		@Override
		public int getColumnCount() {
			return getDelegate().getColumnCount(getView());
		}
		
		@Override
		public String getColumnName(int column) {
			//TODO This is just a hot implementation!
			return String.valueOf(getView().getTableColumAtIndex(column).getIdentifier());
		}

		@Override
		public int getRowCount() {
			return getDelegate().getRowCount(getView());
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			return getView().getTableColumAtIndex(columnIndex).getContentDataSource().getObject(rowIndex, getView());
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return getDelegate().isCellEditable(row,
					getView().getTableColumAtIndex(col));
		}

		@Override
		public Class<?> getColumnClass(int col) {
			Class<?> c = getView().getTableColumAtIndex(col).getContentDataSource().getColumnClass();
			if (c != null)
				return c;

			return super.getColumnClass(col);
		}

		@Override
		public void setValueAt(Object value, int row, int col) {
			logger.debug("cell val=" + value);
			getView().getTableColumAtIndex(col).getContentDataSource().setObject(value, row, getView());
		}

		public void tableCellUpdated(int row, int col) {
			fireTableCellUpdated(row, col);
		}

		public void tableDataChanged() {
			fireTableDataChanged();
		}

		public void tableRowsDeleted(int first, int last) {
			fireTableRowsDeleted(first, last);
		}

		public void tableRowsUpdated(int first, int last) {
			fireTableRowsUpdated(first, last);
		}

		public void tableRowsInserted(int first, int last) {
			fireTableRowsInserted(first, last);
		}

		public void tableStructureChanged() {
			fireTableStructureChanged();
		}
	}

	@Override
	public JTable getViewWidget() {
		return table;
	}

	@Override
	public boolean isEnabled() {
		return table.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return table.isVisible();
	}

	@Override
	public void setEnabled(boolean b) {
		table.setEnabled(b);
	}

	@Override
	public void setVisible(boolean b) {
		table.setVisible(b);
	}

	@Override
	protected void _setViewWidget(Object w) {

		if (!(w instanceof JTable))
			throw new IllegalArgumentException();
		table = (JTable) w;
	}

	@Override
	protected void startListeningForSelectionChanges() {
		table.getSelectionModel().addListSelectionListener(selectionListener);
		logger.debug("startListeningForSelectionChanges");
		
	}

	@Override
	protected void stopListeningForSelectionChanges() {
		table.getSelectionModel()
				.removeListSelectionListener(selectionListener);
		logger.debug("stopListeningForSelectionChanges");
		
	}

	@Override
	protected void breakViewConnection() {
		table.setModel(oldModel);
		super.breakViewConnection();
	}

	@Override
	protected void establishViewConnection() {
		super.establishViewConnection();
		oldModel = table.getModel();
		table.setModel(tableModel);
		applyColumnSettings();
	}

	@Override
	protected void setSelectionInterval(int firstIndex, int lastIndex) {
		table.getSelectionModel().setSelectionInterval(firstIndex,
				lastIndex);
	}
	
	@Override
	protected void addSelectionInterval(int first, int last) {
		table.getSelectionModel().addSelectionInterval(first,last);
	}
	
	@Override
	protected void clearSelection() {
		table.getSelectionModel().clearSelection();
	}

	// /----------------TablePlugIn.Notifications----------------------

	@Override
	public void tableCellUpdated(int row, int col) {
		tableModel.tableCellUpdated(row, col);
	}

	@Override
	public void tableDataChanged() {
		tableModel.tableDataChanged();
	}

	@Override
	public void tableRowsDeleted(int first, int last, boolean updating) {
		tableModel.tableRowsDeleted(first, last);
	}

	@Override
	public void tableRowsUpdated(int first, int last) {
		tableModel.tableRowsUpdated(first, last);
	}

	@Override
	public void tableRowsInserted(int first, int last) {
		tableModel.tableRowsInserted(first, last);
	}

	@Override
	public void tableStructureChanged() {
		tableModel.tableStructureChanged();
		applyColumnSettings();
	}

	private void applyColumnSettings() {
		TableColumnModel colModel = table.getColumnModel();

		for (int i = 0; i < colModel.getColumnCount(); i++) {
			TableColumn swingColumn = colModel.getColumn(i);
			NSTableColumn nsCol = getView().getTableColumAtIndex(i);
			if (nsCol == null)
				continue;

			swingColumn.setIdentifier(nsCol.getIdentifier());
			swingColumn.setCellRenderer(new NSCellRenderer(getView()));
			//TODO header renderer
			swingColumn.setCellEditor(new NSEditorCellTableCellEditor(getView()));
		}
	}
	
	

	private Map<Class<?>,NSEditorCell> defaultEditors = null;

	
	private NSEditorCell createEditorCell(Class<?> clazz) {
		Color editColor = Color.yellow;
		if(Number.class.isAssignableFrom(clazz)){
			NSTextFieldCell cell = new NSTextFieldCell();
			WidgetPlugin plug = new SwingTextWidgetPlugIn();
//			plug.setNativeCellEditor(table.getDefaultEditor(clazz));
			cell.setWidgetPlugIn(plug);
			cell.setFormat(NumberFormat.getInstance());
			cell.setEditBackground(editColor);
			return cell;
		}
		else if(String.class.isAssignableFrom(clazz)){
			NSTextFieldCell cell = new NSTextFieldCell();
			WidgetPlugin plug = new SwingTextWidgetPlugIn();
//			plug.setNativeCellEditor(table.getDefaultEditor(clazz));
			cell.setWidgetPlugIn(plug);
			cell.setEditBackground(editColor);
			return cell;
		}
		else if(Boolean.class.isAssignableFrom(clazz)){
			SimpleCell cell = new SimpleCell();
			WidgetPlugin plug = new SwingToggleWidgetPlugIn();
//			plug.setNativeCellEditor(table.getDefaultEditor(clazz));
			cell.setWidgetPlugIn(plug);
			return cell;
		}
		
		return null;
	}
	
	protected void initDefaultEditors() {
		defaultEditors = new HashMap<Class<?>,NSEditorCell>();
		
		NSEditorCell cell = createEditorCell(Number.class);
		if(cell!=null)
			defaultEditors.put(Number.class, cell);
		
		cell = createEditorCell(String.class);
		if(cell!=null)
			defaultEditors.put(String.class, cell);
		
		//What about Object.class?
	}
	
	@Override
	public NSEditorCell getDefaultEditor(Class<?> key) {
		if(defaultEditors==null){
			initDefaultEditors();
		}
		
		Class<?> clazz = key;
		while(clazz!=null && !defaultEditors.containsKey(clazz)){
			clazz = clazz.getSuperclass();
		}
		
		if(clazz==null)
			return null;
		
		return defaultEditors.get(clazz);
	}
	
	@Override
	public void setDefaultEditor(Class<?> clazz,NSEditorCell cell){
		if(defaultEditors==null){
			defaultEditors = new HashMap<Class<?>,NSEditorCell>();
		}
		
		defaultEditors.put(clazz, cell);
	}
	
	
	private Map<Class<?>,NSCell> defaultRenderer = null;

	private NSCell createRenderCell(Class<?> clazz) {
		NSTextFieldCell cell = new NSTextFieldCell();
		WidgetPlugin plug = new SwingLabelWidgetPlugIn();
//		plug.setNativeCellEditor(table.getDefaultRenderer(clazz));
		cell.setWidgetPlugIn(plug);
		return cell;
	}
	
	protected void initDefaultRenderer() {
		defaultRenderer = new HashMap<Class<?>,NSCell>();
		
		NSCell cell = createRenderCell(Number.class);
		if(cell!=null)
			defaultRenderer.put(Number.class, cell);
		
		cell = createEditorCell(String.class);
		if(cell!=null)
			defaultRenderer.put(String.class, cell);
	}
	
	public NSCell getDefaultRenderer(Class<?> key) {
		if(defaultRenderer==null){
			initDefaultRenderer();
		}
		
		Class<?> clazz = key;
		while(clazz!=null && !defaultRenderer.containsKey(clazz)){
			clazz = clazz.getSuperclass();
		}
		
		if(clazz==null)
			return null;
		
		return defaultRenderer.get(clazz);
	}
	
	public void setDefaultRenderer(Class<?> clazz,NSCell cell){
		if(defaultRenderer==null){
			defaultRenderer = new HashMap<Class<?>,NSCell>();
		}
		
		defaultRenderer.put(clazz, cell);
	}
	
}
