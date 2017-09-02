package de.ceruti.curcuma.keyvaluebinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.ceruti.curcuma.api.core.Range;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.core.Utils;

public class ArrayPropertyBinder<E,T extends List<E>> extends SimplePropertyBinder<T> {
	
//	@Override
//	protected int subjectObservingOptions() {
//		return KVOOption.KeyValueObservingOptionOld | KVOOption.KeyValueObservingOptionNew;
//	}
	
	@Override
	public void syncBinding(KVOEvent change)
			throws KeyValueBindingSyncException {
		switch(change.getKind())
		{
			case KVOEvent.KEY_VALUE_CHANGE_SETTING:
			{
				//OK WE MUST MAKE A COPY otherwise we're producing cycles
				List<? extends E> a = createBackingArray();
				a.addAll((Collection) (change.hasNewValue()? change.getNewValue() : getSubjectValue()));
				setBindingValue(a);
				
				//TODO THIS IS NEEDED FRO Mackie-Midi-Menus BUT causes STACK OVERFLOWS in ArrayControllerTest
				//setBindingValue((Collection) (change.hasNewValue()? change.getNewValue() : getSubjectValue()));
				
				
				//That's ok with both
//				getBindingValue().addAll((Collection) (change.hasNewValue()? change.getNewValue() : getSubjectValue()));
				
//				System.out.println("FIXME: ArrayPropertyBinder.syncBinding:  KVOEvent.KEY_VALUE_CHANGE_SETTING");
			} break;
			
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_INSERTION:
			{
				if (!change.hasNewValue() || !change.getIndexes().isContiguous()) {
					super.syncBinding(change);
					break;
				}
				getBindingValue().addAll(change.getIndexes().firstIndex(), (Collection<? extends E>) change.getNewValue());
			}break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REPLACEMENT:
			{
				if (!change.hasNewValue()) {
					super.syncBinding(change);
					break;
				}
				
				for(Iterator<Range> it = change.getIndexes().rangesIterator(); it.hasNext();){
					Range r=it.next();
					int j=0;
					for(int i=r.getLocation(); i<r.maxRange();i++){
						getBindingValue().set(i, ((List<? extends E>)change.getNewValue()).get(j++));
					}
				}
			}break;
			case KVOEvent.KEY_VALUE_CHANGE_ARRAYELEM_REMOVAL:
			{	
//				if (!change.hasOldValue()) {
//					super.syncBinding(change);
//					break;
//				}
				Utils.removeAtIndexes(getBindingValue(), change.getIndexes());
				
			}
			break;
			

		}

	}

	protected List<E> createBackingArray() {
		return new ArrayList<E>();
	}
}
