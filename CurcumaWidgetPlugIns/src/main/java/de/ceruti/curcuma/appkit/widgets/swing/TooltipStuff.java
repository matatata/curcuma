package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JComponent;
import javax.swing.ToolTipManager;

public class TooltipStuff implements TipInterface {

	
	private JComponent component;
	private String oldTooltip;
	
	public void connect(JComponent w) {
		this.component = w;
	}

	public void disconnect(JComponent w) {
		if(w == this.component)
		{
			this.component = null;
			w.setToolTipText(oldTooltip);
		}
	}

	public void notifyMessage(String m) {
		

		oldTooltip = component.getToolTipText();
		
		component.setToolTipText(m);
		
		final int oldDelay = ToolTipManager.sharedInstance().getInitialDelay();
		ToolTipManager.sharedInstance().setInitialDelay(0);
		
		ToolTipManager.sharedInstance().mouseMoved(
		        new MouseEvent(component, 0, 0, 0,
		                0, 0, // X-Y of the mouse for the tool tip
		                0, false));
		
		TimerTask t = new TimerTask() {

			public void run() {
				if(oldTooltip!=null)
				component.setToolTipText(oldTooltip);
				else
					ToolTipManager.sharedInstance().unregisterComponent(component);
				ToolTipManager.sharedInstance().setInitialDelay(oldDelay);
			}
		};
		Timer timer = new Timer();
		timer.schedule(t, 2000);
		
		

	}

}
