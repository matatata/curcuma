package de.ceruti.curcuma.core.utils;

import java.util.HashSet;

import de.ceruti.curcuma.api.core.utils.Observer;
import de.ceruti.curcuma.api.core.utils.Subject;

public class SubjectImpl<L extends Observer<E>,E> implements Subject<L, E> {

	private HashSet<L> listeners = new HashSet<L>();
	
	@Override
	public void addObserver(L l) {
		listeners.add(l);
	}
	
	@Override
	public void removeObserver(L l) {
		listeners.remove(l);
	}

	@Override
	public void fireEvent(E e) {
		for(L l: listeners) {
			l.handleEvent(e);
		}
	}
}
