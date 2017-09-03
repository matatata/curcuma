package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.ItemSelectable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.text.JTextComponent;

import de.ceruti.curcuma.api.appkit.view.cells.NSBoundedRangeCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSComboCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSTextCell;
import de.ceruti.curcuma.api.appkit.view.cells.NSToggleCell;
import de.ceruti.curcuma.appkit.view.cells.NSActionCellImpl;
import de.ceruti.curcuma.appkit.view.cells.NSComboBoxCell;
import de.ceruti.curcuma.appkit.view.cells.NSTextFieldCell;
import de.ceruti.curcuma.appkit.view.cells.NSToggleCellImpl;
import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;

class NSCellFactoryImpl extends NSCellFactory {
	private final PlugInFactory plugInFactory;
	
	@Override
	public NSTextCell createCellForComponent(JLabel label){
		WidgetPlugin pluf = plugInFactory.createPlugInForComponent(label);
		NSTextFieldCell cell = new NSTextFieldCell();
		cell.setWidgetPlugIn(pluf);
		return cell;
	}
	
	@Override
	public NSTextCell createCellForComponent(JTextComponent t){
		WidgetPlugin pluf = plugInFactory.createPlugInForComponent(t);
		NSTextFieldCell cell = new NSTextFieldCell();
		cell.setWidgetPlugIn(pluf);
		return cell;
	}
	
	@Override
	public NSToggleCell createCellForComponent(ItemSelectable t){
		WidgetPlugin pluf = plugInFactory.createPlugInForComponent(t);
		NSToggleCellImpl cell = new NSToggleCellImpl();
		cell.setWidgetPlugIn(pluf);
		return cell;
	}
	
	@Override
	public NSBoundedRangeCell createCellForComponent(JSlider t){
		WidgetPlugin pluf = plugInFactory.createPlugInForComponent(t);
		NSActionCellImpl cell = new NSActionCellImpl();
		cell.setWidgetPlugIn(pluf);
		return cell;
	}
	
	@Override
	public NSBoundedRangeCell createCellForComponent(JProgressBar t){
		WidgetPlugin pluf = plugInFactory.createPlugInForComponent(t);
		NSActionCellImpl cell = new NSActionCellImpl();
		cell.setWidgetPlugIn(pluf);
		return cell;
	}
	
	@Override
	public NSComboCell createCellForComponent(JComboBox t){
		WidgetPlugin pluf = plugInFactory.createPlugInForComponent(t);
		NSComboBoxCell cell = new NSComboBoxCell();
		cell.setWidgetPlugIn(pluf);
		return cell;
	}
		
	NSCellFactoryImpl() {
		this(new PlugInFactoryImpl());
	}
	
	NSCellFactoryImpl(PlugInFactory plugInFactory){
		this.plugInFactory = plugInFactory;
	}
}
