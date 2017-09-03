package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Component;
import java.awt.ItemSelectable;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;

import de.ceruti.curcuma.appkit.view.cells.WidgetPlugin;
import de.ceruti.curcuma.appkit.view.table.TablePlugin;
import de.ceruti.curcuma.appkit.widgets.AbstractTableViewPlugIn;

class PlugInFactoryImpl extends PlugInFactory {
	
	@Override
	public WidgetPlugin createPlugInForComponent(Component c) {
		if(c instanceof JLabel)
			return createPlugInForComponent((JLabel)c);
		if(c instanceof JTextComponent)
			return createPlugInForComponent((JTextComponent)c);
		if(c instanceof ItemSelectable)
			return createPlugInForComponent((ItemSelectable)c);
		if(c instanceof JSlider)
			return createPlugInForComponent((JSlider)c);
		if(c instanceof JProgressBar)
			return createPlugInForComponent((JProgressBar)c);
		if(c instanceof JComboBox)
			return createPlugInForComponent((JComboBox)c);
		return null;
	}

	@Override
	public WidgetPlugin createPlugInForComponent(JLabel label){
		WidgetPlugin w = new SwingLabelWidgetPlugIn();
		w.setWidget(label);
		return w;
	}
	
	@Override
	public WidgetPlugin createPlugInForComponent(JTextComponent t){
		WidgetPlugin w = new SwingTextWidgetPlugIn();
		w.setWidget(t);
		return w;
	}
	
	@Override
	public WidgetPlugin createPlugInForComponent(ItemSelectable t){
		WidgetPlugin w = new SwingToggleWidgetPlugIn();
		w.setWidget(t);
		return w;
	}
	

	@Override
	public WidgetPlugin createPlugInForComponent(JSlider t) {
		WidgetPlugin pl = new SwingBoundedRangeWidgetPlugIn();
		pl.setWidget(t);
		return pl;
	}

	@Override
	public WidgetPlugin createPlugInForComponent(JProgressBar t) {
		WidgetPlugin pl = new SwingBoundedRangeWidgetPlugIn();
		pl.setWidget(t);
		return pl;
	}
	
	@Override
	public WidgetPlugin createPlugInForComponent(JComboBox t) {
		WidgetPlugin pl = new SwingComboBoxWidgetPlugIn();
		pl.setWidget(t);
		return pl;
	}
	
	@Override
	public TablePlugin createPlugInForComponent(JTable t) {
		AbstractTableViewPlugIn plug = new SwingTableViewPlugIn();
		plug.setViewWidget(t);
		return plug;
	}
}
