package de.ceruti.curcuma.appkit.widgets;

import java.util.Iterator;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.table.NSTableView;
import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.appkit.view.table.TablePlugin;

public abstract class AbstractTableViewPlugIn extends AbstractViewPlugIn
		implements TablePlugin {

	private static Category logger = Logger.getInstance(AbstractTableViewPlugIn.class);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.ceruti.curcuma.appkit.widgets.AbstractViewPlugIn#getDelegate()
	 * covariant return
	 */
	@Override
	public TablePlugin.Delegate getDelegate() {
		Object del = super.getDelegate();

		if (del == null || !(del instanceof TablePlugin.Delegate))
			return TablePlugin.Delegate.Dummy.INSTANCE;

		return (TablePlugin.Delegate) del;
	}
	

	/*
	 * (non-Javadoc)
	 * @see de.ceruti.curcuma.appkit.widgets.AbstractViewPlugIn#getView()
	 */
	@Override
	public NSTableView getView() {
		return (NSTableView) super.getView();
	}
	
	/**
	 * Should be called  when the Selection has been changed Via GUI
	 * @param newSet
	 */
	protected final void selectionDidChangedViaGUI(IndexSet newSet) {
		logger.debug("Selection changed: " + newSet);
		IndexSet current = getDelegate().getSelectionIndexes();
		MutableIndexSet removed = current.mutableIndexSet();
		
		if(removed.equals(newSet))
			return;
		
		if(!getDelegate().shouldChangeSelectionIndexes(AbstractTableViewPlugIn.this, newSet)){
			setSelectionIndexes(current);
			return;
		}
		
		//see if indexes were removed:
		removed.removeIndexes(newSet);
		//see if indexes were added
		MutableIndexSet added = newSet.mutableIndexSet();
		added.removeIndexes(getDelegate().getSelectionIndexes());

		
		if (removed.isEmpty() && added.isEmpty()) {
			getDelegate().selectionIndexesChanged(
					AbstractTableViewPlugIn.this, newSet,
					TablePlugin.Delegate.SELECTION_INDEXES_SET);
			return;
		}
		boolean fineGrainSuccess = true;
		if (!removed.isEmpty()) {
			fineGrainSuccess = getDelegate().selectionIndexesChanged(
					AbstractTableViewPlugIn.this, removed,
					TablePlugin.Delegate.SELECTION_INDEXES_REMOVE);
		}
		if (!added.isEmpty()) {
			if(!getDelegate().selectionIndexesChanged(
					AbstractTableViewPlugIn.this, added,
					TablePlugin.Delegate.SELECTION_INDEXES_ADDED))
				fineGrainSuccess = false;
		}
		if(!fineGrainSuccess) {
			//TODO remove once SELECTION_INDEXES_REMOVE and SELECTION_INDEXES_ADDED are interpreted
			getDelegate().selectionIndexesChanged(
					AbstractTableViewPlugIn.this, newSet,
					TablePlugin.Delegate.SELECTION_INDEXES_SET);
		}
	}
	
	/**
	 * Gui should update it's selection
	 */
	public void setSelectionIndexes(IndexSet indexes) {
		stopListeningForSelectionChanges();
		if(indexes.isContiguous()){
			setSelectionInterval(indexes.firstIndex(),
					indexes.lastIndex());
		}
		else {
			clearSelection();
			for (Iterator<Range> it = indexes.rangesIterator(); it.hasNext();) {
				Range r = it.next();
				addSelectionInterval(r.getLocation(),
						r.maxRange() - 1);
			}
		}
		startListeningForSelectionChanges();
	}
	
	protected abstract void addSelectionInterval(int location, int i);

	protected abstract void clearSelection();

	protected abstract void setSelectionInterval(int firstIndex, int lastIndex);
	
	@Override
	protected void breakViewConnection() {
		super.breakViewConnection();
		stopListeningForSelectionChanges();
	}
	
	@Override
	protected void establishViewConnection() {
		super.establishViewConnection();
		startListeningForSelectionChanges();
	}

	protected abstract void startListeningForSelectionChanges();

	protected abstract void stopListeningForSelectionChanges();

	
}
