package de.ceruti.curcuma.appkit.widgets.swing;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class MBoxTooltip implements TipInterface {
	
	private JComponent component;
	
	public void connect(JComponent w) {
		this.component = w;

	}
	public void disconnect(JComponent w) {
		if(w==this.component){
			this.component = null;
		}
	}

	public void notifyMessage(String m) {
		
		JOptionPane.showMessageDialog(component, m);


		

	}

}
