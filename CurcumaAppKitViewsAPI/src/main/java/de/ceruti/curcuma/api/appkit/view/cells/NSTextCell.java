package de.ceruti.curcuma.api.appkit.view.cells;

import java.text.Format;

import de.ceruti.curcuma.api.appkit.view.NSText;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;

public interface NSTextCell extends NSActionCell, NSText, NSEditorCell {

	Format getFormat();
	void setFormat(Format format);
	void setFormatWhenFocused(Format format);
	Format getFormatWhenFocused();

	Delegate getTextDelegate();
	
	
	interface Delegate extends NSText.Delegate, NSEditorCell.Delegate,
			NSActionCell.Delegate {

		
		class Dummy extends NSText.Delegate.Dummy implements Delegate {

			public static final Delegate INSTANCE = new Delegate.Dummy();
			
			@Override
			public boolean hasValidCellValue(boolean cellState) {
				return cellState;
			}

			
			@Override
			public void cellDidBeginEditing(NSEditorCell sender) {
			}

			@Override
			public void cellDidCancelEditing(NSEditorCell sender) {
			}

			@Override
			public void cellValueDidChangeViaGUI(NSCell sender) {
			}


			@Override
			public void cellDidPerformAction(NSActionCell sender) {
			}

			@Override
			public boolean cellShouldPerformAction(NSActionCell sender) {
				return true;
			}

			@Override
			public Object cellValidateCellValue(NSCell sender, Object c)
					throws ValidationException {
				return c;
			}

			@Override
			public boolean cellShouldAcceptInvalidValue(NSCell sender,
					Object invalidValue, String errMsg) {
				return false;
			}

			@Override
			public boolean cellShouldAcceptInvalidPartialValue(
					NSCell sender, Object invalidValue, String errMsg) {
				return true;
			}

			@Override
			public Object cellValidateDisplayValue(NSCell sender, Object obj)
					throws ValidationException {
				return obj;
			}
		}
	}
}
