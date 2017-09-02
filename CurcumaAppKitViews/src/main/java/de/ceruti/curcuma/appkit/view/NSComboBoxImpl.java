package de.ceruti.curcuma.appkit.view;

import de.ceruti.curcuma.api.appkit.view.NSComboBox;
import de.ceruti.curcuma.api.appkit.view.cells.NSCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSComboCell;

public class NSComboBoxImpl extends NSTextFieldImpl implements NSComboBox {
	public NSComboBoxImpl() {
		super();
	}
	

	
	protected class NSCellDelegate extends NSTextFieldImpl.NSCellDelegate
			implements NSComboCell.Delegate
			{
		
//		public boolean hasValidCellValue(boolean cellState) {
//			
//			return false;
//		}

//		public void textDidBeginEditing(NSText sender) {
//			
//		}
//
//		public void textDidChange(NSText sender) {
//		}
//
//		public void textDidEndEditing(NSText sender) {
//		}
//
//		public void textReceivedFocus(NSText sender) {
//		}
//
//		public boolean textShouldBeginEditing(NSText sender) {
//			return true;
//		}
//
//		public boolean textShouldEndEditing(NSText sender) {
//			return isMarkerState()!=null || ((NSCell)sender).getCellState().hasValidCellValue();
//		}
//		
		
	}
	
	@Override
	protected NSCell.Delegate createDefaultCellDelegate() {
		NSCell.Delegate del = new NSCellDelegate();
		return del;
	}



}
