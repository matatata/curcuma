package de.ceruti.curcuma.appkit.view;

import java.util.List;

import de.ceruti.curcuma.api.appkit.view.ListDataSource;

public abstract class AbstractListDataSource implements ListDataSource {

	protected abstract List getBackingArray();

	public AbstractListDataSource() {
	}

	@Override
	public Object getObjectAtIndex(int index) {
		return getBackingArray().get(index);
	}

	@Override
	public int getSize() {
		return getBackingArray().size();
	}
	
	@Override
	public int indexOf(Object obj) {
		return getBackingArray().indexOf(obj);
	}
}
