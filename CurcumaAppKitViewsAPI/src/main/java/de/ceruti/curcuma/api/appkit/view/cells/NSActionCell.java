package de.ceruti.curcuma.api.appkit.view.cells;

public interface NSActionCell extends NSCell {
	
	Delegate getActionDelegate(); 
	
	interface Delegate extends NSCell.Delegate {
		// Invoked from the gui, if an action has been performed via the ViewPlugin
		void cellDidPerformAction(NSActionCell sender);
		boolean cellShouldPerformAction(NSActionCell sender);
		
		
		
		public class Dummy extends NSCell.Delegate.Dummy implements Delegate{

			@Override
			public void cellDidPerformAction(NSActionCell sender) {
			}

			@Override
			public boolean cellShouldPerformAction(NSActionCell sender) {
				return true;
			}
			public static final Delegate INSTANCE =  new Delegate.Dummy();
		}
	}
}
