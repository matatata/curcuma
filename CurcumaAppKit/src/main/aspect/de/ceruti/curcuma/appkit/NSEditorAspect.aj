/*
This file is part of Curcuma.

Copyright (c) Matteo Ceruti 2009

Curcuma is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Curcuma is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Curcuma.  If not, see <http://www.gnu.org/licenses/>.

*/
package de.ceruti.curcuma.appkit;


import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.NSEditable;
import de.ceruti.curcuma.api.appkit.NSEditor;
import de.ceruti.curcuma.api.appkit.NSEditorEvent;
import de.ceruti.curcuma.api.core.utils.Observer;
import de.ceruti.curcuma.api.core.utils.Subject;
import de.ceruti.curcuma.core.utils.SubjectImpl;



aspect NSEditorAspect {

	static interface NSEditorSupport extends NSEditor {
		/**
		 * @return true if successfully commited
		 */
		boolean onCommit(StringBuffer err);
		/**
		 * If you do not use the Memento Mechanism, you should implement this method
		 */
		boolean revert();
		
		void commitFailed(Object sender);
		
		@Override
		void notifyValidationStatus(boolean success,String message);
//		
//		Object getMemento();
//		void restoreMemento(Object m);
	}
	
	declare parents: @NSDefaultEditor * implements NSEditorSupport;
	
	
	
//	private Object NSEditorSupport.memento = EMPTY_MEMENTO;
	private boolean NSEditorSupport.editing = false;
	private boolean NSEditorSupport.commiting = false;
	private NSEditable NSEditorSupport.controller = null;
	private boolean NSEditorSupport.automaticCommit = true;
	private boolean NSEditorSupport.automaticValidation = true;
	
	private Subject<Observer<NSEditorEvent>, NSEditorEvent> NSEditorSupport.subjectImpl = new SubjectImpl<Observer<NSEditorEvent>, NSEditorEvent>();

	private static Category logger = Logger.getInstance(NSEditorAspect.class);
	
	
//	/**
//	 * 
//	 * @return a valid memento or EMPTY_MEMENTO
//	 */
//	public Object NSEditorSupport.getMemento() {
//		return EMPTY_MEMENTO;
//	}
//	
//	/**
//	 * @param memento
//	 * @throws IllegalArgumentException if you pass in EMPTY_MEMENTO
//	 */
//	public void NSEditorSupport.restoreMemento(Object memento) {
//		if(memento == EMPTY_MEMENTO)
//			throw new IllegalArgumentException("Illegal Memento " + memento);
//	}
	
	private interface NSCommitCallback {
		/**
		 * if an error occurs while attempting to commit, for example if
		 * key-value coding validation fails, an implementation of this method
		 * should typically send the NSView in which editing is being done a
		 * presentError:modalForWindow:delegate:didRecoverSelector:contextInfo:
		 * message, specifying the view's containing window.
		 * 
		 * @param editor
		 * @param didCommit
		 *            the committing has either succeeded or failed
		 * @param context
		 */
		void editorDidCommit(NSEditor editor, boolean didCommit, Object context);
	}

	
	NSCommitCallback NSEditorSupport.createCommitCallback() {
		return new NSCommitCallback(){

			@Override
			public void editorDidCommit(NSEditor editor, boolean didCommit,
					Object context) {
				notifyValidationStatus(didCommit,context != null ? context.toString() : "Changes could not be committed.");
				
			}};
	}
	
	public boolean NSEditorSupport.commitEditing(Object sender) {
		
		if (commiting) {
			logger.debug("commitEditing(): is committing");
			return false;
		}
		
		if (!isEditing()){
			logger.error("commitEditing(): not editing");
			throw new IllegalStateException("commitEditing(): not editing");
		}
		
		boolean ret = commitEditing(createCommitCallback(),sender,null);
		
		if(ret){
			fireEvent(new NSEditorEventImpl(NSEditorEvent.Type.COMMIT,sender));
		}
		
		return ret;
	}


	boolean NSEditorSupport.commitEditing(NSCommitCallback c, Object sender, Object context) {

		if (isCommitting()) {
			logger.debug("commitEditing(NSEditor.NSCommitCallback,Object): is committing");
			return false;
		}
	
		boolean success = false;
		commiting = true;

		try {

			if (!isEditing()) {
				logger.error("commitEditing(NSEditor.NSCommitCallback,Object): not editing");
				throw new IllegalStateException("commitEditing(NSEditor.NSCommitCallback,Object): not editing");
			}
			
			StringBuffer errBuf = new StringBuffer();
			String err = null;
			
			success = onCommit(errBuf);
			
			if (!success) {
				err = errBuf.toString();
				if (c != null)
					c.editorDidCommit(this, false, err);
				return false;
			}

			if (c != null)
				c.editorDidCommit(this, true, context);


			endEditing();

//			memento = getMemento();

		} finally {
			commiting = false;
			if(!success)
				commitFailed(sender);
		}

		return true;
	}
	
	
	public void NSEditorSupport.commitFailed(Object sender) {
		logger.warn("Commit failed");
	}
	
	public boolean NSEditorSupport.isEditing()
	{
		return editing;
	}
	
	void NSEditorSupport.setEditing(boolean e)
	{
		if(this.editing!=e){
			this.editing = e;
			fireEvent(new NSEditorEventImpl( e ? NSEditorEvent.Type.START_EDITING : NSEditorEvent.Type.END_EDITING , this ));
		}
	}

	public void NSEditorSupport.startEditing()
	{	
		if(isCommitting()) {
			logger.debug("startEditing(): is committing");
			return;	
		}
		
		if(isEditing()) {
			logger.debug("startEditing(): already editing");
			return;
		}
		
		logger.debug("start editing");
		
		setEditing(true);
		
		if(getEditingSubject()!=null)
			getEditingSubject().objectDidBeginEditing(this);

		
	}
	
	public void NSEditorSupport.endEditing() {
		
		if(!isEditing()){
			logger.error("endEditing(): not editing");
			throw new IllegalStateException("endEditing(): not editing");
		}
		
		if (getEditingSubject() != null)
			getEditingSubject().objectDidEndEditing(this);
		
		setEditing(false);
		
		
		
		
		logger.debug("end editing");
	}
	
	public boolean NSEditorSupport.isCommitting() {
		return commiting;
	}

	public void NSEditorSupport.discardEditing(Object sender) {
		
		if(isCommitting()) {
			logger.debug("discardEditing(): is committing");
			return;	
		}
		
		if(!isEditing()){
			logger.error("discardEditing(): not editing");
			throw new IllegalStateException("discardEditing(): not editing");
		}
		
		boolean success = false;
		
//		if(memento != EMPTY_MEMENTO) {
//			restoreMemento(memento);
//			memento = getMemento();
//			success = true;
//		}
//		else {
			success = revert();
//		}
		
		if(success){
			endEditing();
			fireEvent(new NSEditorEventImpl(NSEditorEvent.Type.DISCARD, sender));
//			endEditing();
		}
	
	}

	public NSEditable NSEditorSupport.getEditingSubject() {
		return controller;
	}
	
	public void NSEditorSupport.setEditingSubject(NSEditable controller) {
		this.controller = controller;
	}

	public boolean NSEditorSupport.isAutomaticValidation() {
		return automaticValidation;
	}

	public void NSEditorSupport.setAutomaticValidation(boolean automaticValidation) {
		this.automaticValidation = automaticValidation;
	}
	
	public boolean NSEditorSupport.isAutomaticCommit() {
		return automaticCommit;
	}

	public void NSEditorSupport.setAutomaticCommit(boolean automaticCommit) {
		this.automaticCommit = automaticCommit;
	}
	
	public void NSEditorSupport.addObserver(Observer<NSEditorEvent> l) {
		subjectImpl.addObserver(l);
	}


	public void NSEditorSupport.fireEvent(NSEditorEvent e) {
		subjectImpl.fireEvent(e);
	}


	public void NSEditorSupport.removeObserver(Observer<NSEditorEvent> l) {
		subjectImpl.removeObserver(l);
	}


	
//	/**
//	 * Placeholder for the unitialized Memento;
//	 */
//	private final static Object EMPTY_MEMENTO = new String("EMPTY_MEMENTO");
	

	
	
}
