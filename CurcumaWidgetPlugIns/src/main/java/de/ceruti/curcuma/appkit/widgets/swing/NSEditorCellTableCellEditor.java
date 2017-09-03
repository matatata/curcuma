/**
 * 
 */
package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditor;
import de.ceruti.curcuma.api.appkit.view.NSText;
import de.ceruti.curcuma.api.appkit.view.cells.NSActionCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSComboCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSTextCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.appkit.NSDefaultEditor;
import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;

@NSDefaultEditor
class NSEditorCellTableCellEditor extends AbstractCellEditor implements NSEditor,
		TableCellEditor, NSCell.Delegate, NSTextCell.Delegate,
		NSActionCell.Delegate, NSEditorCell.Delegate, NSComboCell.Delegate {
	private static Category logger = Logger
			.getInstance(NSEditorCellTableCellEditor.class);

	private NSEditorCell editorCell;
	private NSTableColumn editingColumn;
	private int editingRow;
	private NSTableView view;
	
	//NONO this is not really correct
	private Object originalWidget = null;

	public NSEditorCellTableCellEditor(NSTableView view) {
		this.view = view;
		addCellEditorListener(new CellEditorListener() {
			
			@Override
			public void editingStopped(ChangeEvent arg0) {
				logger.info("Stopped editing");
			}
			
			@Override
			public void editingCanceled(ChangeEvent arg0) {
				logger.info("Canceled editing");
			}
		});
	}

	public NSTableView getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.AbstractCellEditor#isCellEditable(java.util.EventObject)
	 */
	@Override
	public boolean isCellEditable(EventObject anEvent) {
		if (anEvent instanceof MouseEvent) {
			return ((MouseEvent) anEvent).getClickCount() >= 2;
		}
		return true;
	}

	
	@Override
	public boolean hasValidCellValue(boolean cellState) {
		return cellState;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.AbstractCellEditor#shouldSelectCell(java.util.EventObject )
	 */
	@Override
	public boolean shouldSelectCell(EventObject arg0) {
		logger.debug("shouldSelectCell() event=" + arg0);
		return super.shouldSelectCell(arg0);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, int, int)
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		logger.debug("Start edititing cell");
		TableCellEditor nativeEditor = null;
		
		NSEditorCell cell = getView().preparedEditorCell(value, isSelected,
				row, column);
		WidgetPlugin plugIn = null;
		
		if(cell==null){
			throw new IllegalStateException(); // no editor
//			cell=new SimpleCell();
		}
		

		//TODO simplyfiy it. Do not allow cells without widget!
		
		plugIn = (WidgetPlugin)cell.getImplementation();
		
		if(plugIn==null || plugIn.isDummy()){
//			TableCellEditor defaultEditor = table.getDefaultEditor(table
//					.getColumnClass(column));
//			plugIn=PlugInFactory.get().createPlugInForComponent(defaultEditor.getTableCellEditorComponent(table, value,
//					isSelected, row, column));
			throw new IllegalStateException("missing WidgetPlugIn");
		}
		
		if(plugIn.getNativeCellEditor()!=null){
			if(plugIn.getNativeCellEditor() instanceof TableCellEditor){
				nativeEditor = (TableCellEditor) plugIn.getNativeCellEditor();
			}
			else {
				logger.error("this cell's native table-cell-editor is not a Swing-TableCellEditor! I won't use it.");
			}
		}
		
		setState(cell, row, column);		
		

		Component c = null;
		

		if (nativeEditor != null) {
			c = nativeEditor.getTableCellEditorComponent(table, value,
					isSelected, row, column);
			if (cell != null) {
				plugIn.setWidget(c);
				cell.updateDisplayValue();
			}
		} else if (plugIn.getWidget() == null) {
			TableCellEditor defaultEditor = table.getDefaultEditor(table
					.getColumnClass(column));
			c = defaultEditor.getTableCellEditorComponent(table, value,
					isSelected, row, column);
			plugIn.setWidget(c);
			cell.updateDisplayValue();
		} else {
			c = (Component) plugIn.getWidget();
			
			if (c instanceof TableCellEditor) {
				c = ((TableCellEditor) c).getTableCellEditorComponent(table,
						plugIn.getPlugInValue(), isSelected, row, column);
			} else {
				drawComponent(c, table, plugIn.getPlugInValue(), isSelected,
						row, column);
			}
		}

		return c;
	}

	@Override
	public Object getCellEditorValue() {
		Object v = getEditorCell().getCellValue();
		try {
			v = editingColumn.getContentDataSource().validateObject(v, editingRow, getView());
		} catch (ValidationException e) {
			throw new IllegalStateException();
		}
		return v;
	}
	
	
	private void setState(NSEditorCell cell, int row, int col){
		
		editingRow = row;
		setEditorCell(cell);
		if(col>=0)
			editingColumn = getView().getTableColumAtIndex(col);
		else {
			editingColumn = null;
		}
	}

	@Override
	public boolean stopCellEditing() {
		return stopCellEditing(true);
	}
	
	public boolean stopCellEditing(boolean endNSEditing) {
		logger.debug("stop editing");

		if (!getEditorCell().stopCellEditing())
			return false;
		
		try {
			editingColumn.getContentDataSource().validateObject(getEditorCell().getCellValue(), editingRow, getView());
		} catch (ValidationException e) {
			getEditorCell().notifyMessage(e.getMessage());
			return false;
		}
		
		boolean ret = false;
		try {
			ret = super.stopCellEditing();
		}catch(IllegalArgumentException e){
		}
		
		if (ret) {
			setState(null, -1, -1);
			if(endNSEditing && isEditing())
			{
				endEditing();
				setEditingSubject(null);
			}
		}

		if(getEditorCell() instanceof NSText)
			((NSText)getEditorCell()).textEndEditing();
		
		return ret;
	}

	@Override
	public void cancelCellEditing() {
		cancelCellEditing(true);
	}

	private void cancelCellEditing(boolean endNSEditing) {
		logger.debug("cancel editing");
		getEditorCell().cancelCellEditing();
		setState(null, -1, -1);
		super.cancelCellEditing();
		if(endNSEditing && isEditing())
			endEditing();
	}
	
	@Override
	public void cellValueDidChangeViaGUI(NSCell sender) {
	}

	@Override
	public void textDidBeginEditing(NSText sender) {
	}

	@Override
	public void textDidChange(NSText sender) {
	}

	@Override
	public void textDidEndEditing(NSText sender) {
	}

	@Override
	public void textReceivedFocus(NSText sender) {
		sender.selectAll();
	}

	@Override
	public boolean textShouldBeginEditing(NSText sender) {
		return true;
	}

	@Override
	public boolean textShouldEndEditing(NSText sender) {
		return getEditorCell().hasValidCellValue();
	}

	@Override
	public void cellDidPerformAction(NSActionCell sender) {
		stopCellEditing(true);
	}

	@Override
	public boolean cellShouldPerformAction(NSActionCell sender) {
		return getEditorCell().hasValidCellValue();
	}

	@Override
	public void cellDidBeginEditing(NSEditorCell sender) {
		if(!isEditing()) {
			setEditingSubject(getView().getEditingSubject());
			startEditing();
		}
	}

	@Override
	public void cellDidCancelEditing(NSEditorCell sender) {
		cancelCellEditing();
	}

	@Override
	public Object cellValidateCellValue(NSCell sender, Object c) throws ValidationException {
		return c;
	}
	
	@Override
	public Object cellValidateDisplayValue(NSCell sender, Object obj) throws ValidationException {
		return obj;
	}

	@Override
	public boolean cellShouldAcceptInvalidValue(NSCell sender,
			Object invalidValue, String errMsg) {
		return false;
	}

	@Override
	public boolean cellShouldAcceptInvalidPartialValue(NSCell sender,
			Object invalidValue, String errMsg) {
		return true;
	}

	private void drawComponent(Component c, JTable table, Object value,
			boolean isSelected, int row, int column) {
	}

	private NSEditorCell getEditorCell() {
		return editorCell;
	}

	private void setEditorCell(NSEditorCell cell) {
		if (this.editorCell != cell) {
			if (this.editorCell != null) {
				WidgetPlugin plugin = (WidgetPlugin)this.editorCell.getImplementation();
				if (this.originalWidget != plugin.getWidget()) {
					plugin.setWidget(this.originalWidget); // reset Component
				}
				this.editorCell.setDelegate(null); // detach delegate
			}
			this.editorCell = cell;
			this.originalWidget = null;
			if (this.editorCell != null) {
				this.editorCell.setDelegate(this);
				originalWidget = ((WidgetPlugin)this.editorCell.getImplementation()).getWidget();
			}
		}
	}
	
//NSEditor Stuff:
	
	public boolean onCommit(StringBuffer err) {
		return stopCellEditing(false);
	}

	public void commitFailed() {
	}
	
	public boolean revert() {
		cancelCellEditing(false);
		return true;
	}
	
	@Override
	public void notifyValidationStatus(boolean success,String message) {
	}

}