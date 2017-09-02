package de.ceruti.curcuma.appkit.view.table;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSComboCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.table.NSTableColumn;
import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.appkit.view.table.TableColumnDataSource;
import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KVOOption;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvaluebinding.BindingInfoImpl;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;
import de.ceruti.curcuma.keyvaluecoding.KeyPathUtilities;

public class NSTableCol extends NSObjectImpl implements NSTableColumn {
	private NSCell dataCell;
	private NSCell headerCell;
	private NSEditorCell editorCell;
	private Object identifier;
	private NSTableView tableView;

	public NSTableCol() {
		
	}
	
	@Override
	public Set<String> exposedBindings() {
		Set<String> e = new HashSet<String>();
			
		e.addAll(super.exposedBindings());
		
		if(getDataCell()!=null)
		{
			e.addAll(getDataCell().exposedBindings());
		}
		if(getEditorCell()!=null)
		{
			e.addAll(getEditorCell().exposedBindings());
		}
		return Collections.unmodifiableSet(e);
	}
	
	@Override
	public NSCell getDataCell() {
		return dataCell;
	}

	@Override
	public void setDataCell(NSCell dataCell) {
		this.dataCell = dataCell;
	}

	@Override
	public NSCell getHeaderCell() {
		return headerCell;
	}

	@Override
	public void setHeaderCell(NSCell headerCell) {
		this.headerCell = headerCell;
	}

	@Override
	public Object getIdentifier() {
		return identifier;
	}

	@Override
	public NSTableView getTableView() {
		return this.tableView;
	}

	@Override
	public void setIdentifier(Object o) {
		this.identifier = o;
	}

	@Override
	public void setTableView(NSTableView v) {
		this.tableView = v;
	}

	@Override
	public NSEditorCell getEditorCell() {
		return editorCell;
	}

	@Override
	public void setEditorCell(NSEditorCell editorCell) {
		this.editorCell = editorCell;
	}
	
	//BINDINGS
	
	@Override
	public KeyValueBinder createBinderForBinding(String binding, Object observedObject) {
		if(binding.equals(NSComboCell.ContentBinding)
				|| binding.equals(NSCell.CellValueBinding))
			return new ValueBinder(false);
		if(binding.equals(NSComboCell.ContentValuesBinding))
			return new ValueBinder(true);
		
		return super.createBinderForBinding(binding,observedObject);
	}
	
	protected class MyBindingInfoImpl extends BindingInfoImpl {
		protected String controllerKey;
		protected String modelKeypath;
		
		protected MyBindingInfoImpl(String name, Object observedObj,
				String subjectKeyPath, BindingOptions options) {
			super(name, observedObj, subjectKeyPath, options);
			StringBuilder controllerKeyBuf= new StringBuilder();
			StringBuilder modelKpBuf = new StringBuilder();
			
			if(KeyPathUtilities.extractHeadAndTail(subjectKeyPath, controllerKeyBuf, modelKpBuf)){
				controllerKey = controllerKeyBuf.toString();
				modelKeypath = modelKpBuf.toString();
			}
			else {
				controllerKey = subjectKeyPath;
				modelKeypath = null;
			}
		}


	}
	
	protected class ValueBinder implements KeyValueBinder, KVObserver, TableColumnDataSource {

		private boolean isContentValues;
		
		private MyBindingInfoImpl info;
		
		@Override
		public void observeValue(String keypath, KeyValueObserving object,
				KVOEvent change, Object context) {
			
			if(info.nestingLevelUpdatingSubject()>0)
				return;
			
			NSTableColumn col = (NSTableColumn)context;
			
			switch (change.getKind()) {
			case KVOEvent.KEY_VALUE_CHANGE_SETTING:
				break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
				break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
				break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
				getTableView().tableCellUpdated(change.getIndexes(), getTableView().indexOfColumn(col));
				break;
			}
			
		}

		
		
		
		public ValueBinder(boolean isContentValues) {
			this.isContentValues = isContentValues;
		}
		
		
		@Override
		public BindingInfo bind(KeyValueBindingCreation creation,
				String binding, Object subject, String subjectKeyPath,
				BindingOptions options) {
			info = new MyBindingInfoImpl(binding,subject,subjectKeyPath,options);

			((KeyValueObserving) subject).addObserver(this, subjectKeyPath,
					NSTableCol.this, KVOOption.KeyValueObservingOptionNone);
		
			if(isContentValues)
				setContentValuesDataSource(this);
			else
				setContentDataSource(this);
			return info;
		}
		
		@Override
		public void unbind() {
				((KeyValueObserving) info.getObservedObject())
						.removeObserver(this, info.getObservedKeyPath());
				
				setContentDataSource(null);
		}
		
		@Override
		public void syncBinding(KVOEvent change) throws KeyValueBindingSyncException  {
			NSTable.updateFromChange(getTableView(),change);
		}

		@Override
		public boolean allowsReverseSynchronisation() {
			return false;
		}

		@Override
		public BindingInfo getBindingInfo() {
			return info;
		}

		@Override
		public void syncSubject(KVOEvent notification)
				throws KeyValueBindingSyncException {
		}

		@Override
		public void updateBinding() throws KeyValueBindingSyncException {
		}

		@Override
		public Object validateBindingValue(Object newBindingValue)
				throws ValidationException, KeyValueBindingSyncException {
			return newBindingValue;
		}

		@Override
		public Object validateSubjectValue(Object newSubjectValue)
				throws ValidationException, KeyValueBindingSyncException {
			return newSubjectValue;
		}
		
		@Override
		public void setObject(Object o, int row, NSTableView sender) {
			if(isContentValues)
				throw new UnsupportedOperationException();
			info.enterUpdatingSubject();
			Object obj = info.getObservedObject();
			obj = ((KeyValueCoding)obj).getValueForKey(info.controllerKey);
			obj = ((List)obj).get(row);
			((KeyValueCoding)obj).setValueForKeyPath(o,info.modelKeypath);
			info.exitUpdatingSubject();
		}
		
		@Override
		public Object validateObject(Object o, int row, NSTableView sender) throws ValidationException {
			if(isContentValues)
				throw new UnsupportedOperationException();
			Object obj = info.getObservedObject();
			obj = ((KeyValueCoding)obj).getValueForKey(info.controllerKey);
			obj = ((List)obj).get(row);
			return ((KeyValueCoding)obj).validateValueForKeyPath(o,info.modelKeypath);
		}
		
		@Override
		public Object getObject(int row, NSTableView sender) {
			Object obj = info.getObservedObject();
			obj = ((KeyValueCoding)obj).getValueForKey(info.controllerKey);
			obj = ((List)obj).get(row);
			if(info.modelKeypath==null)
				return obj;
			return ((KeyValueCoding)obj).getValueForKeyPath(info.modelKeypath);
		}
		
		@Override
		public Class<?> getColumnClass() {
			return null;
		}
	}

	
	private TableColumnDataSource contentValuesDS;
	private TableColumnDataSource contentDS;

	@Override
	public TableColumnDataSource getContentValuesDataSource() {
		return contentValuesDS;
	}

	@Override
	public void setContentValuesDataSource(TableColumnDataSource s) {
		this.contentValuesDS = s;
	}

	@Override
	public TableColumnDataSource getContentDataSource() {
		return this.contentDS;
	}

	@Override
	public void setContentDataSource(TableColumnDataSource s) {
		this.contentDS = s;
	}
}
