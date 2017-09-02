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
		
		
	}
	
	@Override
	protected NSCell.Delegate createDefaultCellDelegate() {
		NSCell.Delegate del = new NSCellDelegate();
		return del;
	}



}
