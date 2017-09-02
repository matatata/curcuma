/**
 * 
 */
package de.ceruti.curcuma.keyvaluebinding;

import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluebinding.KeyValueBindingCreation;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;


public interface KeyValueBinder {
		
		/**
		 * Establish a binding to a subject's value
		 * @param creation TODO
		 * @param binding
		 * @param subject
		 * @param subjectKeyPath
		 * @param options
		 * @return
		 */
		BindingInfo bind(KeyValueBindingCreation creation,String binding,Object subject,String subjectKeyPath, BindingOptions options);
		
		/**
		 * Return the BindingInfo-Object that has been created via {@link #bind(KeyValueBindingCreation, String, Object, String, BindingOptions)}
		 * @return null if no binding has been established yet or a previous binding had been removed ({@link #unbind()})
		 */
		BindingInfo getBindingInfo();
		
		
		/**
		 * Remove the binding
		 */
		void unbind();
		
		
		/**
		 * If the observed subject's value changes we must update the binding here.
		 * @param notification the event that just modified the observed subject 
		 */
		void syncBinding(KVOEvent notification) throws KeyValueBindingSyncException;
		
		/**
		 * If the binding changes we might want to update the observed subject as well (bi-directional binding).
		 * @return true if this direction is supported
		 */
		boolean allowsReverseSynchronisation();
		
		/**
		 * If the bindings's value changes we must update the observed subject's value here.
		 * If {@link #allowsReverseSynchronisation()} returns false, this method should throw an UnsupportedOperationException
		 * @param notification the event that just modified the binding's value
		 * @throws KeyValueBindingSyncException if sync failed
		 */
		void syncSubject(KVOEvent notification) throws KeyValueBindingSyncException;
		
		Object validateSubjectValue(Object newSubjectValue) throws ValidationException, KeyValueBindingSyncException;
		Object validateBindingValue(Object newBindingValue) throws ValidationException, KeyValueBindingSyncException;
		
		
		/**
		 * udate the bindings value from the subject.
		 * Might be as simple as reading the subjects value and setting it as binding value
		 */
		void updateBinding() throws KeyValueBindingSyncException;
	
	}