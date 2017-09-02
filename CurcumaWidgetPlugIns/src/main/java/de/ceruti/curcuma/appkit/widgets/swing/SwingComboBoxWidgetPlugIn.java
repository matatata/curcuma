package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxEditor;
import javax.swing.JComboBox;
import javax.swing.text.JTextComponent;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import de.ceruti.curcuma.api.appkit.view.cells.NSEditorCell;
import de.ceruti.curcuma.appkit.view.cells.ComboBoxCellWidgetPlugin;
import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;
import de.ceruti.curcuma.appkit.widgets.awt.AWTWidgetPlugIn;

public class SwingComboBoxWidgetPlugIn extends AWTWidgetPlugIn implements
		ComboBoxCellWidgetPlugin
		{
	
	private ComboBoxModel comboBoxModel = new ComboBoxModel();
	private Object selectedObject = null;

	
	public SwingComboBoxWidgetPlugIn() {
		super();
	}
	

	@Override
	public JComboBox getWidget() {
		return (JComboBox)super.getWidget();
	}


	@Override
	public void setEditable(boolean e) {
		getWidget().setEditable(e);
	}
	
	@Override
	public boolean isEditable() {
		return getWidget().isEditable();
	}
	
	public void setValidityStatus(boolean isValid, String reason) {
	}

	@Override
	public boolean isEnabled() {
		return getWidget().isEnabled();
	}
	
	@Override
	public void setEnabled(boolean e) {
		getWidget().setEnabled(e);
	}
	
	public ComboBoxCellWidgetPlugin.Delegate getWidgetAssociation() {
		Object assoc = super.getWidgetAssociation();
		if (assoc instanceof ComboBoxCellWidgetPlugin.Delegate)
			return (ComboBoxCellWidgetPlugin.Delegate) assoc;
		return ComboBoxCellWidgetPlugin.Delegate.Dummy.INSTANCE;
	}
	
	@Override
	public Object getPlugInValue() {
		return selectedObject;
	}


	@Override
	protected void _setPlugInValue(Object p) {
		comboBoxModel.setSelectedItem(p);
		
	}
	
	public void setSelectedIndex(int d) {
		getWidget().setSelectedIndex(d);
	}
	
	public int getSelectedIndex() {
		return getWidget().getSelectedIndex();
	}

	@Override
	public void breakCellConnection() {
		super.breakCellConnection();
		getWidget().setModel(null);
		getWidget().setEditor(null); //TODO set old editor
	}
	
	
	

	@Override
	public void establishCellConnection() {
		super.establishCellConnection();
		getWidget().setModel(comboBoxModel);
		
		NSEditorCell edCell = getWidgetAssociation().getEditorCell(); 
		if(edCell!=null ){
			WidgetPlugin plugIn = (WidgetPlugin) edCell.getImplementation();
			if(plugIn.getWidget()==null){
				plugIn.setWidget(getWidget().getEditor().getEditorComponent());
			}
			getWidget().setEditor(new MyEditor(getWidgetAssociation().getEditorCell()));
		}
	}
	
	
	
	
	private class MyEditor implements ComboBoxEditor {
		private java.util.Set<ActionListener> listeners = new HashSet<ActionListener>();
		private Component component;
		
		public MyEditor(NSEditorCell cell) {
			this.component = (Component) ((WidgetPlugin) cell
					.getImplementation()).getWidget();
		}

		public Component getEditorComponent() {
			return component;
		}
		
		public void addActionListener(ActionListener l) {
		}
		
		private void fire(){
			for(ActionListener l : listeners){
				l.actionPerformed(new ActionEvent(this,0,""));
			}
		}

		public Object getItem() {
			return selectedObject;
		}

		public void removeActionListener(ActionListener l) {
			listeners.remove(l);
		}

		public void selectAll() {
		}

		public void setItem(Object anObject) {
		}
	}

	private WidgetPlugin createEditorPlugIn() {
		return new SwingTextWidgetPlugIn();
	}

	public void listContentsChanged(int index0, int index1) {
		comboBoxModel.listContentsChanged(index0, index1);

	}

	public void listIntervalAdded(int index0, int index1) {
		comboBoxModel.listIntervalAdded(index0, index1);
	}

	public void listIntervalRemoved(int index0, int index1) {
		comboBoxModel.listIntervalRemoved(index0, index1);
	}
		
	class ComboBoxModel extends AbstractListModel implements
			javax.swing.ComboBoxModel {

		public Object getSelectedItem() {
			return getPlugInValue();
		}
		
		public void setSelectedItem(Object anItem) {
			Object newValue = anItem;
			int selectedIndex=getSelectedIndex();
			if(ObjectUtils.equals(selectedObject, newValue))
				return;
			
			selectedObject = newValue;
			fireContentsChanged(this, -1, -1);
			
			if (notifyAssociationEnabled()) {
				getWidgetAssociation().plugInDidBeginEditing(
						SwingComboBoxWidgetPlugIn.this);
				if(getWidgetAssociation().plugInValueDidChangeViaGui(
						SwingComboBoxWidgetPlugIn.this)){
				}
				SwingComboBoxWidgetPlugIn.this.actionPerformed();
			}
		}

		public void listContentsChanged(int index0, int index1) {
			fireContentsChanged(this, index0, index1);
		}

		public void listIntervalAdded(int index0, int index1) {
			fireIntervalAdded(this, index0, index1);
		}

		public void listIntervalRemoved(int index0, int index1) {
			fireIntervalRemoved(this, index0, index1);
		}

		public Object getElementAt(int index) {
			return getWidgetAssociation().getDisplayValueAtIndex(index);
		}

		public int getSize() {
			return getWidgetAssociation().getDisplayValuesCount();
		}
	}
	
	
	private JTextComponent getEditorTextComponent() {
		return (JTextComponent) getWidget().getEditor().getEditorComponent();
	}
	
}
