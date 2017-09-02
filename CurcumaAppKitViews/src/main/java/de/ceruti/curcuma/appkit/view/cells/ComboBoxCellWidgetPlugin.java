package de.ceruti.curcuma.appkit.view.cells;

import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;


public interface ComboBoxCellWidgetPlugin extends EditorWidgetPlugin,
	ActionControlWidgetPlugin  {

	int getSelectedIndex();
	void setSelectedIndex(int d);
	
	
	interface Delegate extends EditorWidgetPlugin.Delegate , ActionControlWidgetPlugin.Delegate {

		Object getDisplayValueAtIndex(int index);
		int getDisplayValuesCount();
		int indexOfDisplayValue(Object display);

		/**
		 * Will be used for the editable-text part, if the combo-box component is editable. Tip. Use a TextFieldCell
		 * @return
		 */
		NSEditorCell getEditorCell();
		void setEditorCell(NSEditorCell cell);
		
		class Dummy extends
		EditorWidgetPlugin.Delegate.Dummy implements Delegate {

			@Override
			public Object getDisplayValueAtIndex(int index) {
				return null;
			}

			@Override
			public int getDisplayValuesCount() {
				return 0;
			}

			@Override
			public int indexOfDisplayValue(Object display) {
				return -1;
			}

			@Override
			public void pluginDidPerformAction(ActionControlWidgetPlugin sender) {
			}

			@Override
			public boolean pluginShouldPerformAction(
					ActionControlWidgetPlugin sender) {
				return true;
			}
			
			public static final Delegate INSTANCE = new Delegate.Dummy();

			@Override
			public NSEditorCell getEditorCell() {
				return null;
			}

			@Override
			public void setEditorCell(NSEditorCell cell) {
			}
		}
	}

	void listContentsChanged(int index0, int index1);

	void listIntervalAdded(int index0, int index1);

	void listIntervalRemoved(int index0, int index1);

	
	class Dummy extends WidgetPlugin.Dummy implements ComboBoxCellWidgetPlugin{

		@Override
		public int getSelectedIndex() {
			return 0;
		}

		@Override
		public void listContentsChanged(int index0, int index1) {
		}

		@Override
		public void listIntervalAdded(int index0, int index1) {
		}

		@Override
		public void listIntervalRemoved(int index0, int index1) {
		}

		@Override
		public void setSelectedIndex(int d) {
		}
		
		public static final ComboBoxCellWidgetPlugin INSTANCE = new ComboBoxCellWidgetPlugin.Dummy();
	}
	
}