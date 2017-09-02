package de.ceruti.curcuma.api.appkit.view;

public interface ListDataSource {
	Object getObjectAtIndex(int index);
	int getSize();
	int indexOf(Object obj);
}
