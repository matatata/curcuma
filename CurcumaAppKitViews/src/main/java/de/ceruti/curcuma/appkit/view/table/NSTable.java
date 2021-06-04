package de.ceruti.curcuma.appkit.view.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditable;
import de.ceruti.curcuma.api.appkit.controller.NSArrayController;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.appkit.view.table.TableDataSource;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.appkit.view.NSViewBase;
import de.ceruti.curcuma.appkit.view.cells.SimpleCell;
import de.ceruti.curcuma.core.Factory;
import de.ceruti.curcuma.keyvaluebinding.AbstractScalarValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

public class NSTable extends NSViewBase implements NSTableView {

	private static transient Logger logger = LogManager.getLogger(NSTable.class);
	private IndexSet selectionIndexes = Factory.emptyIndexSet();
	private List<?> contentArray = new ArrayList<Object>();
	private List<NSTableColumn> tableColumns = new ArrayList<NSTableColumn>();
	private TableDataSource dataSource;

	public NSTable() {
		exposeBinding(SelectionIndexesBinding, IndexSet.class);
		exposeBinding(ContentArrayBinding, List.class);
	}

	@Override
	public IndexSet getSelectionIndexes() {
		return selectionIndexes;
	}

	@Override
	public TablePlugin getViewPlugIn() {
		return (TablePlugin) super.getViewPlugIn();
	}

	@Override
	@PostKVONotifications
	public void setSelectionIndexes(IndexSet indexes) {
		_setSelectionIndexesNoNotify(indexes, true);
	}
	
	protected void _setSelectionIndexesNoNotify(IndexSet indexes,
			boolean tellPlugin) {
		this.selectionIndexes = indexes.immutableIndexSet();
		if (tellPlugin) {
			// This won't fire Notifications
			getViewPlugIn().setSelectionIndexes(indexes);
		}
	}

	protected class NSTableViewPlugInDelegate implements ViewPlugIn.Delegate,
			TablePlugin.Delegate {

		

		@Override
		public IndexSet getSelectionIndexes() {
			return NSTable.this.getSelectionIndexes();
		}
		
		/*
		 * (non-Javadoc)
		 * @see de.ceruti.curcuma.appkit.view.table.TablePlugin.Delegate#selectionIndexesChanged(de.ceruti.curcuma.appkit.view.table.TablePlugin, de.ceruti.curcuma.api.core.IndexSet, int)
		 */
		@Override
		public boolean selectionIndexesChanged(TablePlugin sender,
				IndexSet newSelection, int changeType) {


			switch (changeType) {
			case TablePlugin.Delegate.SELECTION_INDEXES_SET:
				NSTable.this._setSelectionIndexesNoNotify(newSelection, false);
				try {
					NSTable.this.updateObservableBoundToBinding(SelectionIndexesBinding);
				} catch (KeyValueBindingSyncException e) {
					logger.error(e);
					return false;
				}
				return true;
			case TablePlugin.Delegate.SELECTION_INDEXES_ADDED:
			case TablePlugin.Delegate.SELECTION_INDEXES_REMOVE:
			{
				BindingInfo ifo = NSTable.this.getInfoForBinding(SelectionIndexesBinding);
				if(ifo==null || !(ifo.getObservedObject() instanceof NSArrayController))
					return false;

				IndexSet oldSelection = this.getSelectionIndexes();
				boolean success = false;
				if(changeType==TablePlugin.Delegate.SELECTION_INDEXES_ADDED)
					success = ((NSArrayController)ifo.getObservedObject()).addSelectionIndexes(newSelection);
				else
					success = ((NSArrayController)ifo.getObservedObject()).removeSelectionIndexes(newSelection);
				
				if(!success)
				{
					NSTable.this._setSelectionIndexesNoNotify(oldSelection,true);
				}
				
				return true;
			}
			default:
				assert(false);
			}

			return false;

		}

		@Override
		public boolean shouldChangeSelectionIndexes(TablePlugin sender,
				IndexSet newSelection) {
			return getDelegate().shouldChangeSelectionIndexes(newSelection);
		}

		@Override
		public int getColumnCount(NSTableView sender) {
			if(dataSource!=null)
				return dataSource.getColumnCount(sender);
			return sender.getTableColumnCount();
		}

		@Override
		public int getRowCount(NSTableView sender) {
			if(dataSource!=null)
				return dataSource.getRowCount(sender);
			return 0;
		}

		@Override
		public boolean isCellEditable(int row, NSTableColumn col) {
			return getDelegate().isCellEditable(row, col);
		}



	}
	
	/**
	 * Overide this if you want to provide another default Delegate
	 * 
	 * @return
	 */
	@Override
	protected ViewPlugIn.Delegate createDefaultViewPlugInDelegate() {
		return new NSTableViewPlugInDelegate();
	}

	protected List getContentArray() {
		return contentArray;
	}

	protected void setContentArray(List contentArray) {
		this.contentArray = contentArray;
	}

	@Override
	public void addTableColumn(NSTableColumn col) {
		tableColumns.add(col);
		col.setTableView(this);
	}

	@Override
	public boolean removeTableColumn(NSTableColumn col) {
		if(tableColumns.remove(col)){
			col.setTableView(null);
			return true;
		}
		
		return false;
	}

	@Override
	public int indexOfColumnWithIdentifier(Object identifier) {
		for (int i = 0; i < tableColumns.size(); i++) {
			NSTableColumn c = tableColumns.get(i);
			if (c.getIdentifier().equals(identifier))
				return i;
		}

		return -1;
	}

	@Override
	public List<NSTableColumn> getTableColumns() {
		return Collections.unmodifiableList(tableColumns);
	}
	
	
	@Override
	public int getTableColumnCount() {
		return tableColumns.size();
	}

	@PostKVONotifications
	public void setTableColumns(List<NSTableColumn> cols) {
		this.tableColumns = cols;
	}
	
	@Override
	public NSTableColumn getTableColumAtIndex(int columnIndex) {
		if (columnIndex >= tableColumns.size())
			return null;

		return tableColumns.get(columnIndex);
	}

	@Override
	public NSTableColumn tableColumnWithIdentifier(Object identifier) {
		for (NSTableColumn c : tableColumns) {
			if (c.getIdentifier().equals(identifier))
				return c;
		}

		return null;
	}

	@Override
	public int indexOfColumn(NSTableColumn col) {
		return tableColumns.indexOf(col);
	}

	@Override
	public TableDataSource getDataSource() {
		return dataSource;
	}

	@Override
	public void setDataSource(TableDataSource dataSource) {
		if (this.dataSource != dataSource) {
			this.dataSource = dataSource;
			getViewPlugIn().tableStructureChanged();
		}
	}

	// ------- unsure about this -----------------//

	@Override
	public void tableDataChanged() {
		getViewPlugIn().tableDataChanged();
	}

	protected void tableCellUpdated(int row, int col) {
		getViewPlugIn().tableCellUpdated(row, col);
	}

	protected void tableRowsDeleted(int first, int last,boolean updating) {
		getViewPlugIn().tableRowsDeleted(first, last, updating);
	}

	protected void tableRowsInserted(int first, int last) {
		getViewPlugIn().tableRowsInserted(first, last);
	}

	protected void tableRowsUpdated(int first, int last) {
		getViewPlugIn().tableRowsUpdated(first, last);
	}

	@Override
	public void tableStructureChanged() {
		getViewPlugIn().tableStructureChanged();
	}

	@Override
	public void tableRowsInserted(IndexSet rows) {
		for (Iterator<Range> it = rows.rangesIterator(); it.hasNext();) {
			Range r = it.next();
			tableRowsInserted(r.getLocation(), r.maxRange() - 1);
		}
	}

	@Override
	public void tableRowsUpdated(IndexSet rows) {
		for (Iterator<Range> it = rows.rangesIterator(); it.hasNext();) {
			Range r = it.next();
			tableRowsUpdated(r.getLocation(), r.maxRange() - 1);
		}
	}

	@Override
	public void tableRowsDeleted(IndexSet rows) {
		for (Iterator<Range> it = rows.rangesIterator(); it.hasNext();) {
			Range r = it.next();
			tableRowsDeleted(r.getLocation(), r.maxRange() - 1,it.hasNext());
		}
	}
	
	@Override
	public void tableCellUpdated(IndexSet rows, int col) {
		for (Iterator<Integer> it = rows.iterator(); it.hasNext();) {
			tableCellUpdated(it.next(), col);
		}
	}

	@Override
	public NSCell preparedCell(Object contentValue, boolean isSelected, boolean hasFocus,
			int row, int col) {

		NSTableColumn column = getTableColumAtIndex(col);
		Object value = contentValue;
		NSCell cell = column.getDataCell();
		//TODO ask delegate for cell
		//cell = getDelegate().getCellFor(row,col)

		
		if(column.getContentValuesDataSource()!=null)
		{
			//ignore value
			value = column.getContentValuesDataSource().getObject(row, this);
		}
		
		if(cell==null)
			cell = new SimpleCell();
		
		if(cell!=null){
			cell.setCellValue(value);
			return cell;
		}
		
		return null;
	}

	@Override
	public NSEditorCell preparedEditorCell(Object value, boolean isSelected, int row,
			int col) {

		NSTableColumn column = getTableColumAtIndex(col);
		NSEditorCell cell = column.getEditorCell();
		
		if(cell==null)
			cell = getDelegate().getEditorCell(row,col);
		
		//still null
		if(cell==null)
			cell = getDefaultEditor(value == null ? Object.class : value.getClass());
		
		if (cell != null) {
			cell.setCellValue(value);
			return cell;
		}
		
		return null;
		
		//you may ignore this:
	}
	

	
	private Delegate delegate;
	
	@Override
	public Delegate getDelegate() {
		return delegate != null ? delegate : Delegate.Dummy.INSTANCE;
	}
	
	@Override
	public void setDelegate(Delegate d) {
		this.delegate = d;
	}
	
	
	private Map<Class<?>,NSEditorCell> defaultEditors = null;

	@Override
	public NSEditorCell getDefaultEditor(Class<?> key) {
		if(defaultEditors==null){
			defaultEditors = new HashMap<Class<?>,NSEditorCell>();
		}
		
		Class<?> clazz = key;
		while(clazz!=null && !defaultEditors.containsKey(clazz)){
			clazz = clazz.getSuperclass();
		}
		
		if(clazz==null)
			return getViewPlugIn().getDefaultEditor(key);
		
		return defaultEditors.get(clazz);
	}
	
	@Override
	public void setDefaultEditor(Class<?> clazz,NSEditorCell cell){
		if(defaultEditors==null){
			defaultEditors = new HashMap<Class<?>,NSEditorCell>();
		}
		
		defaultEditors.put(clazz, cell);
	}
	

	private NSEditable editingSubject;
	
	
	@Override
	public NSEditable getEditingSubject() {
		return editingSubject;
	}

	@Override
	public void setEditingSubject(NSEditable editingSubject) {
		this.editingSubject = editingSubject;
	}

	@Override
	public void bind(String binding, Object observable, String withKeyPath,
			BindingOptions options) {
		super.bind(binding, observable, withKeyPath, options);
		if (isBound(binding) && ContentArrayBinding.equals(binding)
				&& getInfoForBinding(binding).getObservedObject() instanceof NSEditable) {
			setEditingSubject((NSEditable) observable);
		}
	}
	
	//-----------Binding //
	
	@Override
	public KeyValueBinder createBinderForBinding(String binding, Object observedObject) {
		if(binding.equals(ContentArrayBinding))
			return new ContentArrayBinder();
		else if(binding.equals(SelectionIndexesBinding))
			return new SelectionIndexesBinder();
		return super.createBinderForBinding(binding,observedObject);
	}
	
	protected class SelectionIndexesBinder extends AbstractScalarValueBinder {

		@Override
		protected Object getBindingValue() throws KeyValueBindingSyncException {
			return getSelectionIndexes();
		}
		
		@Override
		protected void setBindingValue(Object newValue)
				throws KeyValueBindingSyncException {
			setSelectionIndexes((IndexSet) newValue);
		}

		@Override
		protected IndexSet validateBinding(Object newValue)
				throws ValidationException, KeyValueBindingSyncException {
			if (!(newValue instanceof IndexSet))
				throw new ValidationException();
			return (IndexSet) newValue;
		}
		
	}
	
	protected class ContentArrayBinder extends AbstractScalarValueBinder {
		
		public ContentArrayBinder() {
		
		}
		

		@Override
		public BindingInfo bind(KeyValueBindingCreation creation,
				String binding, Object subject, String subjectKeyPath,
				BindingOptions options) {
			BindingInfo info = super.bind(creation, binding, subject, subjectKeyPath, options);
			setDataSource(new SubjectArrayDataSource());
			return info;
		}
		
		@Override
		public void unbind() {
			super.unbind();
			setDataSource(null);
		}
		
		@Override
		public void syncBinding(KVOEvent change) throws KeyValueBindingSyncException  {
			super.syncBinding(change);
			updateFromChange(NSTable.this,change);
		}
		
		class SubjectArrayDataSource extends AbstractTableDataSource{
			@Override
			protected List contentArray() {
				return (List) getBindingValue();
			}

			@Override
			public boolean isCellEditable(int row, NSTableColumn col) {
				return getDelegate().isCellEditable(row,col);
			}
		}
		
		@Override
		protected Object getBindingValue() {
			return getContentArray();
		}

		@Override
		protected void setBindingValue(Object newValue) {
			setContentArray((List) newValue);
		}

		@Override
		protected Object validateBinding(Object newValue)
				throws ValidationException {
			return newValue;
		}
		
	}
	
	/**
	 * makes the gui reflect changes of given event
	 */
	public static void updateFromChange(NSTableView table, KVOEvent change) {
		switch (change.getKind()) {
		case KVOEvent.KEY_VALUE_CHANGE_SETTING:
			table.tableDataChanged();
			break;
		case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
			table.tableRowsDeleted(change.getIndexes());
			break;
		case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
			table.tableRowsInserted(change.getIndexes());
			break;
		case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
			table.tableRowsUpdated(change.getIndexes());
			break;
		}
	}
	
	

}
