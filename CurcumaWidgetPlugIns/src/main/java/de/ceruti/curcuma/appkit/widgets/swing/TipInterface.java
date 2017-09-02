package de.ceruti.curcuma.appkit.widgets.swing;

import javax.swing.JComponent;

public interface TipInterface {

	public abstract void disconnect(JComponent w);

	public abstract void connect(JComponent w);

	public abstract void notifyMessage(String m);

}