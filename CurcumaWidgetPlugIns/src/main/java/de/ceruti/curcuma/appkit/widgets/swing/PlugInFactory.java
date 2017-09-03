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

public abstract class PlugInFactory {
	public abstract WidgetPlugin createPlugInForComponent(Component c);
	
	public abstract WidgetPlugin createPlugInForComponent(JLabel label);

	public abstract WidgetPlugin createPlugInForComponent(JTextComponent t);

	public abstract WidgetPlugin createPlugInForComponent(ItemSelectable t);

	public abstract WidgetPlugin createPlugInForComponent(JSlider t);

	public abstract WidgetPlugin createPlugInForComponent(JProgressBar t);

	public abstract WidgetPlugin createPlugInForComponent(JComboBox t);
	
	public abstract TablePlugin createPlugInForComponent(JTable t);
	
	private static PlugInFactory instance=new PlugInFactoryImpl();
	
	public static PlugInFactory get(){
		return instance;
	}
}