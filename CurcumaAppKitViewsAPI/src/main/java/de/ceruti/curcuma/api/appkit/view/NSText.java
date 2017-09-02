package de.ceruti.curcuma.api.appkit.view;

/**
 * @deprecated
 * @author matteo
 *
 */
@Deprecated
public interface NSText {
	/*
	 * Does not send messages to the delegate
	 */
	void textBeginEditing();

	/*
	 * Does not send messages to the delegate
	 */
	void textEndEditing();
	
	void textRequestFocus();

	void setText(String t);

	String getText();

	void selectAll();

	void clearSelection();
	
	interface Delegate {
		void textReceivedFocus(NSText sender);

		void textDidBeginEditing(NSText sender);

		void textDidEndEditing(NSText sender);

		/**
		 * optionally invoked by NSText-Implementation
		 * 
		 * @param sender
		 * @return
		 */
		boolean textShouldEndEditing(NSText sender);

		/**
		 * optionally invoked by NSText-Implementation
		 * 
		 * @param sender
		 * @return
		 */
		boolean textShouldBeginEditing(NSText sender);

		/**
		 * invoked if text had been changed either by the user-interface or
		 * otherwise
		 * 
		 * @param sender
		 */
		void textDidChange(NSText sender);


		class Dummy implements NSText.Delegate {
			@Override
			public void textDidBeginEditing(NSText editor) {
			}

			@Override
			public void textDidEndEditing(NSText editor) {
			}

			@Override
			public boolean textShouldBeginEditing(NSText editor) {
				return true;
			}

			@Override
			public boolean textShouldEndEditing(NSText editor) {
				return true;
			}

			public void textDidEditText(NSText sender) {
			}

			@Override
			public void textDidChange(NSText sender) {
			}
			
			@Override
			public void textReceivedFocus(NSText sender) {
			}


			public static final NSText.Delegate INSTANCE = new Dummy();
		}
	}

	
}
