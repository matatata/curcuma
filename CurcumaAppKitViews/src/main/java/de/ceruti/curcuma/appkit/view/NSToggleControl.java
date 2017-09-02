package de.ceruti.curcuma.appkit.view;

import de.ceruti.curcuma.api.appkit.view.cells.NSActionCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;

public class NSToggleControl extends NSEditingControl {

	public NSToggleControl() {
		super();
	}

	protected class NSCellDelegate extends NSEditingControl.NSCellDelegate {
		public NSCellDelegate() {
		}

		@Override
		public void cellDidPerformAction(NSActionCell sender) {
			startEditing();
			super.cellDidPerformAction(sender);
		}
	}

	@Override
	protected NSCell.Delegate createDefaultCellDelegate() {
		return new NSCellDelegate();
	}

}
