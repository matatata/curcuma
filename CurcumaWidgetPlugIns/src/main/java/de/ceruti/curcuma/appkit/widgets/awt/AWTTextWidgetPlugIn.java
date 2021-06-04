package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Logger;


public class AWTTextWidgetPlugIn extends AbstractAWTTextWidgetPlugIn {
	private static Logger logger = org.apache.logging.log4j.LogManager.getLogger(AWTTextWidgetPlugIn.class);

	public AWTTextWidgetPlugIn() {
		super();
	}

	@Override
	public TextComponent getWidget() {
		return (TextComponent) super.getWidget();
	}


	@Override
	protected void _setViewWidget(Object w) {
	}

	@Override
	public TextComponent getViewWidget() {
		return getWidget();
	}

	
	@Override
	protected void startListeningForTextEvents() {
		logger.debug("startListeningForTextEvents()");
		getWidget().addTextListener(myTextListener);
	}

	@Override
	protected void stopListeningForTextEvents() {
		logger.debug("stopListeningForTextEvents()");
		getWidget().removeTextListener(myTextListener);
	}

	protected MyTextListener myTextListener = new MyTextListener();

	protected class MyTextListener implements TextListener {

		@Override
		public void textValueChanged(TextEvent e) {
			System.out.println(e);
			textHasChanged();
		}

	}

	
	@Override
	protected void _setPlugInValue(Object p) {
		setText(p/*.stringValue()*/.toString());
	}
	
	private void enableNotifications() {
		super.setNotifyAssociationEnabled(true);
	}
	
	/**
	 * Overridden because on awt
	 * setNotifyAssociationAboutPlugInValueChangesViaGUI(true) must be called
	 * later
	 * @param doNotify
	 */
	@Override
	protected void setNotifyAssociationEnabled(boolean doNotify) {
		if (doNotify) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					enableNotifications();
				}
			});
		} else
			super.setNotifyAssociationEnabled(false);
	}

	@Override
	protected void addActionListener(ActionListener l) {
		if (getWidget() instanceof TextField)
			((TextField) getWidget()).addActionListener(l);
	}

	@Override
	protected void addFocusListener(FocusListener l) {
		getWidget().addFocusListener(l);
	}

	@Override
	protected String getString() {
		return getWidget().getText();
	}

	@Override
	protected void removeActionListener(ActionListener l) {
		if (getWidget() instanceof TextField)
			((TextField) getWidget()).removeActionListener(l);
	}

	@Override
	protected void removeFocusListener(FocusListener l) {
		getWidget().removeFocusListener(l);
	}

	@Override
	protected void setString(String s) {
		getWidget().setText(s);
	}

	@Override
	public void clearSelection() {
		getWidget().select(0, 0);
	}

	@Override
	public void selectAll() {
		getWidget().selectAll();
	}
	
	@Override
	public Object getBackground() {
		return getWidget().getBackground();
	}

	@Override
	public void setBackground(Object nat) {
		getWidget().setBackground((Color)nat);
	}
	
	@Override
	public Object getForeground() {
		return getWidget().getForeground();
	}

	@Override
	public void setForeground(Object nat) {
		getWidget().setForeground((Color)nat);
	}


}
