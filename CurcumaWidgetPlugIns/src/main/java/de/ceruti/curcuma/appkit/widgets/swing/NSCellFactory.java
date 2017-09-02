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

public abstract class NSCellFactory {

	public abstract NSTextCell createCellForComponent(JLabel label);

	public abstract NSTextCell createCellForComponent(JTextComponent t);

	public abstract NSToggleCell createCellForComponent(ItemSelectable t);

	public abstract NSBoundedRangeCell createCellForComponent(JSlider t);

	public abstract NSBoundedRangeCell createCellForComponent(JProgressBar t);

	public abstract NSComboCell createCellForComponent(JComboBox t);
	
	private static NSCellFactory instance = new NSCellFactoryImpl();
	
	public static NSCellFactory create(PlugInFactory plugInFactory){
		return instance;
	}

}