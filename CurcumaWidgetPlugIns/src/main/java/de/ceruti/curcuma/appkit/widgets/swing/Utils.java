package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JColorChooser;
import javax.swing.SwingUtilities;

import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.keyvaluecoding.KeyValueCodingWrapper;

public class Utils {
	public static void runMeOnEventDispatchThread(Runnable r) {
		if (SwingUtilities.isEventDispatchThread())
			r.run();
		else {
			SwingUtilities.invokeLater(r);
		}
	}

	public static void runMeLater(Runnable r) {
		SwingUtilities.invokeLater(r);
	}
	
	public static ActionListener createColorPickerForKeyPath(final Object target, final String propertyPath, final String message,final Container parent){
		return new ActionListener() {
			
			private Color chooseColor(Color c) {
				c = JColorChooser.showDialog(parent,
						message, c);
				return c;
			}
			
			@Override
			public void actionPerformed(ActionEvent e) {
				KeyValueCoding kvc = KeyValueCodingWrapper.wrapIfNecessary(target);
				
				Color c = (Color) kvc.getValueForKeyPath(propertyPath);
				c=chooseColor(c);
				if(c!=null)
				{
					kvc.setValueForKeyPath(c, propertyPath);
				}
			}
		};
	}
}
