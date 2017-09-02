package de.ceruti.curcuma.appkit.view;

import de.ceruti.curcuma.api.appkit.view.ListDataSource;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;

abstract class AbstractKVOListDataSource extends AbstractListDataSource
		implements ListDataSource {

	@Override
	public Object getObjectAtIndex(int index) {
		Object object = super.getObjectAtIndex(index);
		if (object != null && object instanceof KeyValueCoding
				&& getModelKeyPath() != null) {
			return ((KeyValueCoding) object)
					.getValueForKeyPath(getModelKeyPath());
		}
		return object;
	}
	

	protected abstract String getModelKeyPath();
}
