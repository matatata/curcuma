package de.ceruti.curcuma.appkit.view.cells;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditor;
import de.ceruti.curcuma.api.appkit.view.ListDataSource;
import de.ceruti.curcuma.api.appkit.view.NSText;
import de.ceruti.curcuma.api.appkit.view.cells.NSActionCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSComboCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSTextCell;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingException;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.appkit.view.AbstractListDataSource;
import de.ceruti.curcuma.appkit.view.SimpleListDataSource;
import de.ceruti.curcuma.foundation.NSObjectImpl;
import de.ceruti.curcuma.keyvaluebinding.ArrayPropertyBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBinder;
import de.ceruti.curcuma.keyvaluebinding.KeyValueBindingSyncException;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

public class NSComboBoxCell extends NSActionCellImpl implements NSComboCell,
		ComboBoxCellWidgetPlugin.Delegate {

	
	
	private static transient Category logger = Logger.getInstance(NSComboBoxCell.class);
	private ListDataSource contentDataSource;
	private ListDataSource displayDataSource;
	
	private NSEditorCell edCell;
	

	public NSComboBoxCell() {
		exposeBinding(ContentBinding, List.class);
		exposeBinding(ContentValuesBinding, List.class);
		exposeBinding(CellValueIndexBinding, int.class);
	}

	
	
	@Override
	protected final Object cell2DisplayValue(Object c) throws ValidationException {
		
		if(displayDataSource==null)
			return super.cell2DisplayValue(c);
		
		c = super.cell2DisplayValue(c);
		
		int index = contentDataSource.indexOf(c);
		if(index==-1){
			throw new ValidationException();
		}
		
		return displayDataSource
		.getObjectAtIndex(index);
	}
	
	@Override
	protected final Object display2cellValue(Object d) throws ValidationException {
		if (displayDataSource==null)
			return super.display2cellValue(d);
		
		int index = getWidgetPlugIn().getSelectedIndex();
		if(index==-1){
			index=indexOfDisplayValue(super.display2cellValue(d));
		}
		if(index==-1) {
		//still no valid element found?
			if(d!=null) //not null, but not found :-( 
				throw new ValidationException();
			else//null... ok
				return super.display2cellValue(d);
		}

		return contentDataSource
				.getObjectAtIndex(index);
	}
	
	@Override
	public ComboBoxCellWidgetPlugin getWidgetPlugIn() {
		return (ComboBoxCellWidgetPlugin) super.getWidgetPlugIn();
	}

	@Override
	public int getCellValueIndex() {
		return contentDataSource.indexOf(getCellValue());
	}
	
	@Override
	@PostKVONotifications
	public void setCellValueIndex(int s) {
		if(s>=0)
			setCellValue(contentDataSource.getObjectAtIndex(s));
		else
			setCellValue(null);
	}
	
	public static Set<String> keyPathsForValuesAffectingValueForKey(String key) {
		Set<String> s = NSObjectImpl.keyPathsForValuesAffectingValueForKey(key);
		if(CellValueIndexBinding.equals(key))
			s.add(CellValueBinding);
		return s;
	}
	
	
	@Override
	public final int getDisplayValuesCount() {
		if(displayDataSource!=null)
			return displayDataSource.getSize();
		if(contentDataSource==null)
			return 0;
		return contentDataSource.getSize();
	}
	
	@Override
	public final int indexOfDisplayValue(Object display) {
		if(displayDataSource!=null)
			return displayDataSource.indexOf(display);
		if(contentDataSource==null)
			return -1;
		return contentDataSource.indexOf(display);
	}
	
	@Override
	public final Object getDisplayValueAtIndex(int index) {
		if(displayDataSource!=null)
			return displayDataSource.getObjectAtIndex(index);
		if(contentDataSource==null)
			return null;
		return contentDataSource.getObjectAtIndex(index);
	}
	
	@Override
	public final void listContentsChanged(IndexSet indexes) {
		for (Iterator<Range> it = indexes.rangesIterator(); it.hasNext();) {
			Range r = it.next();
			getWidgetPlugIn().listContentsChanged(r.getLocation(),
					r.maxRange() - 1);
		}
		updateDisplayValue();
	}

	@Override
	public final void listIntervalAdded(IndexSet indexes) {
		for (Iterator<Range> it = indexes.rangesIterator(); it.hasNext();) {
			Range r = it.next();
			getWidgetPlugIn().listIntervalAdded(r.getLocation(),
					r.maxRange() - 1);
		}
		updateDisplayValue();
	}

	@Override
	public final void listIntervalRemoved(IndexSet indexes) {
		for (Iterator<Range> it = indexes.rangesIterator(); it.hasNext();) {
			Range r = it.next();
			getWidgetPlugIn().listIntervalRemoved(r.getLocation(),
					r.maxRange() - 1);
		}
	
		updateDisplayValue();
	}

	@Override
	public final void listReloadData() {
		getWidgetPlugIn().listContentsChanged(0, Integer.MAX_VALUE);
	}
	

	@Override
	public final ListDataSource getDisplayDataSource() {
		return displayDataSource;
	}

	@Override
	public final void setDisplayDataSource(ListDataSource displayDataSource) {
		this.displayDataSource = displayDataSource;
	}
	
	@Override
	public final ListDataSource getContentDataSource() {
		return contentDataSource;
	}

	@Override
	public final void setContentDataSource(ListDataSource contentDataSource) {
		this.contentDataSource = contentDataSource;
	}

	@Override
	public final void setContentDataArray(List elements) {
		setContentDataSource(new SimpleListDataSource(elements));
	}
	
	@Override
	public final void setDisplayDataArray(List elements) {
		setDisplayDataSource(new SimpleListDataSource(elements));
	}

	///Binding
	
	@Override
	public KeyValueBinder createBinderForBinding(String binding, Object observedObject) {
		if (binding.equals(ContentBinding))
			return new ContentBinder(){
				@Override
				protected void setDataSource(ListDataSource s) {
					setContentDataSource(s);
				}};
		else if (binding.equals(ContentValuesBinding))
			return new ContentBinder(){
				@Override
				protected void setDataSource(ListDataSource s) {
					setDisplayDataSource(s);
				}
				
				//only here? Yes!
				@Override
				protected List<Object> createBackingArray() {
					return new ComboboxContentList(new ArrayList<Object>());
				}
		};
		return super.createBinderForBinding(binding,observedObject);
	}

	abstract class ContentBinder extends ArrayPropertyBinder<Object,List<Object>> {

		@Override
		public void syncBinding(KVOEvent change) throws KeyValueBindingSyncException {
			super.syncBinding(change);
			switch (change.getKind()) {
			case KVOEvent.KEY_VALUE_CHANGE_SETTING:
				listReloadData();
				break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
				listIntervalRemoved(change.getIndexes());
				break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
				listIntervalAdded(change.getIndexes());
				break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
				listContentsChanged(change.getIndexes());
				break;
			}
		}

		@Override
		public BindingInfo bind(KeyValueBindingCreation creation,
				String binding, Object subject, String subjectKeyPath,
				BindingOptions options) {
			setDataSource(new AbstractListDataSource() {
				@Override
				protected List getBackingArray() {
					return getBindingValue();
				}
				
			});
			return super.bind(creation, binding, subject, subjectKeyPath,
					options);
		}

		@Override
		public void unbind() {
			setDataSource(null);
			super.unbind();
		}
		
		@Override
		public boolean allowsReverseSynchronisation() {
			return false;
		}
		
		protected abstract void setDataSource(ListDataSource s);
	}
	
	
	public static class ComboboxContentList extends AbstractList<Object>{
		private List<ListItem> backingList;

		public ComboboxContentList(List<Object> backingList) {
			this.backingList = new ArrayList<ListItem>(backingList.size());
			for (Object s : backingList) {
				this.backingList.add(new ListItem(s));
			}
		}

		@Override
		public Object get(int i) {
			return this.backingList.get(i);
		}

		@Override
		public int size() {
			return this.backingList.size();
		}
		
		@Override
		public Object set(int index, Object element) {
			return this.backingList.set(index, new ListItem(element));
		}
		
		@Override
		public void add(int index, Object element) {
			this.backingList.add(index, new ListItem(element));
		}
		
		@Override
		public Object remove(int index) {
			return this.backingList.remove(index);
		}
		
		@Override
		public int indexOf(Object o) {
			if(o instanceof ListItem)
				return super.indexOf(o);
			
			int i=0;
			for(ListItem it:this.backingList){
				if(ObjectUtils.equals(o, it.object))
					return i;
				i++;
			}
			
			return -1;
		}
		
	}
	
	public static class ListItem {
		private final Object object;

		public ListItem(Object object) {
			super();
			this.object = object;
		}
		
		@Override
		public String toString() {
			return String.valueOf(object);
		}
	}
	
	
	@Override
	public NSComboCell.Delegate getComboDelegate() {
		Object del = super.getDelegate();
		if(del!=null && del instanceof NSComboCell.Delegate)
			return (NSComboCell.Delegate)del;
		return NSComboCell.Delegate.Dummy.INSTANCE;
	}

	public NSTextCell.Delegate getTextCellDelegate() {
		Object del = super.getDelegate();
		if (del != null && del instanceof NSTextCell.Delegate)
			return (NSTextCell.Delegate) del;
		return NSTextCell.Delegate.Dummy.INSTANCE;
	}
	
	@Override
	public final void cancelCellEditing() {
		updateDisplayValue();
	}

	@Override
	public final boolean stopCellEditing() {
		return updateCellValue();
	}
	
	@Override
	public void editorDidEndEditing(NSEditor editor) {
	}

	@Override
	public void editorDidStartEditing(NSEditor editor) {
	}
	
	
	///delegate methods

	@Override
	public final void plugInDidBeginEditing(EditorWidgetPlugin sender) {
		getComboDelegate().cellDidBeginEditing(this);
	}

	@Override
	public final void plugInDidCancelEditing(EditorWidgetPlugin sender) {
		getComboDelegate().cellDidCancelEditing(this);
	}
	
	
	@Override
	public void pluginDidPerformAction(ActionControlWidgetPlugin sender) {
		super.pluginDidPerformAction(sender);
	}
	
	
	
	@Override
	public final void updateDisplayValue() {
		super.updateDisplayValue();
		if (getEditorCell() != null) {
			getEditorCell().setDisplayValue(getDisplayValue());
			getEditorCell().updateCellValue();
		}
	}
	
	@Override
	public boolean plugInValueDidChangeViaGui(WidgetPlugin sender) {
		boolean ret = super.plugInValueDidChangeViaGui(sender);
		
		if (getEditorCell() != null) {
			getEditorCell().setDisplayValue(getDisplayValue());
			getEditorCell().updateCellValue();
		}
		try {
			updateObservableBoundToBinding(CellValueIndexBinding);
		} catch (KeyValueBindingSyncException e) {
			throw new KeyValueBindingException(e);
		}
		return ret;
	}
	
	@Override
	public NSEditorCell getEditorCell() {
		return edCell;
	}
	
	private NSCell.Delegate oldDel;
	
	@Override
	public void setEditorCell(NSEditorCell cell) {
		if(edCell!=null){
			edCell.setDelegate(oldDel);
		}
		
		oldDel = cell.getDelegate();
		cell.setDelegate(new DefaultEditorCellDelegate());
		
		
		edCell = cell;
		
		
	}
	
	class DefaultEditorCellDelegate implements NSEditorCell.Delegate, NSActionCell.Delegate, NSTextCell.Delegate {

		@Override
		public void cellDidBeginEditing(NSEditorCell sender) {
			getEditorDelegate().cellDidBeginEditing(sender);
		}

		@Override
		public void cellDidCancelEditing(NSEditorCell sender) {
			getEditorDelegate().cellDidCancelEditing(sender);
		}

		
		@Override
		public boolean cellShouldAcceptInvalidPartialValue(NSCell sender,
				Object invalidValue, String errMsg) {
			return getDelegate().cellShouldAcceptInvalidPartialValue(sender, invalidValue, errMsg);
		}

		@Override
		public boolean cellShouldAcceptInvalidValue(NSCell sender,
				Object invalidValue, String errMsg) {
			return getDelegate().cellShouldAcceptInvalidValue(sender, invalidValue, errMsg);
		}

		
		@Override
		public void cellValueDidChangeViaGUI(NSCell sender) {

			setDisplayValue(sender.getDisplayValue());
			if(updateCellValue())
				getDelegate().cellValueDidChangeViaGUI(NSComboBoxCell.this);
		}

		@Override
		public Object cellValidateCellValue(NSCell sender, Object c) throws ValidationException {
			return getDelegate().cellValidateCellValue(sender, c);
		}
		
		@Override
		public Object cellValidateDisplayValue(NSCell sender, Object obj) throws ValidationException {
			return getDelegate().cellValidateDisplayValue(sender, obj);
		}
		

		@Override
		public void cellDidPerformAction(NSActionCell sender) {
			getActionDelegate().cellDidPerformAction(NSComboBoxCell.this);
		}

		@Override
		public boolean cellShouldPerformAction(NSActionCell sender) {
			return getActionDelegate().cellShouldPerformAction(sender);
		}

		@Override
		public void textDidBeginEditing(NSText sender) {
			getTextCellDelegate().textDidBeginEditing(sender);
		}

		@Override
		public void textDidChange(NSText sender) {
			getTextCellDelegate().textDidChange(sender);
		}

		@Override
		public void textDidEndEditing(NSText sender) {
			getTextCellDelegate().textDidEndEditing(sender);
		}

		@Override
		public void textReceivedFocus(NSText sender) {
			getTextCellDelegate().textReceivedFocus(sender);
		}

		@Override
		public boolean textShouldBeginEditing(NSText sender) {
			return getTextCellDelegate().textShouldBeginEditing(sender);
		}
		
		@Override
		public boolean hasValidCellValue(boolean cellState) {
			return cellState;
		}

		@Override
		public boolean textShouldEndEditing(NSText sender) {
			return getTextCellDelegate().textShouldEndEditing(sender);
		}
		
	}


}
