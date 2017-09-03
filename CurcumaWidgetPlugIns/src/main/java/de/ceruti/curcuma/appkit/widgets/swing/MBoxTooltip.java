package de.ceruti.curcuma.appkit.widgets.swing;

import javax.swing.JComponent;
import javax.swing.JOptionPane;

public class MBoxTooltip implements TipInterface {
	
	private JComponent component;
	
	@Override
	public void connect(JComponent w) {
		this.component = w;

	}
	@Override
	public void disconnect(JComponent w) {
		if(w==this.component){
			this.component = null;
		}
	}

	@Override
	public void notifyMessage(String m) {
		
		JOptionPane.showMessageDialog(component, m);


		

	}

}
