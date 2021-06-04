package de.ceruti.curcuma.appkit.widgets.awt;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.appkit.widgets.AbstractTextWidgetPlugIn;

public abstract class AbstractAWTTextWidgetPlugIn extends
		AbstractTextWidgetPlugIn {

	/**
	 * invokes actionPerformed()
	 */
	protected class DefaultActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			AbstractAWTTextWidgetPlugIn.this.actionPerformed();
		}

	}

	protected class DefaultFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			AbstractAWTTextWidgetPlugIn.this.focusGained();
		}

		@Override
		public void focusLost(FocusEvent e) {
			AbstractAWTTextWidgetPlugIn.this.focusLost();
		}

	}
	
	private KeyListener keyListener = new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode()==KeyEvent.VK_ESCAPE)
			{
//				getWidgetAssociation().plugInDidCancelEditing(AbstractAWTTextWidgetPlugIn.this);
				cancelKeyTyped();
				e.consume();
			}
		}
	};


	private static Logger logger = org.apache.logging.log4j.LogManager.getLogger(AbstractAWTTextWidgetPlugIn.class);

	private Component component;

	private ActionListener myActionListener;

	private FocusListener myFocusListener;

	private Color origBGColor = null;

	protected AbstractAWTTextWidgetPlugIn() {
	}

	@Override
	public Component getWidget() {
		return component;
	}

	@Override
	public boolean hasFocus() {
		return getWidget().hasFocus();
	}

	@Override
	public boolean isEnabled() {
		return component.isEnabled();
	}

	@Override
	public boolean isVisible() {
		return component.isVisible();
	}

	@Override
	public void notifyMessage(String m) {
		System.out.println(m);
	}

	@Override
	public final void requestFocus() {
		getWidget().requestFocusInWindow();
	}
	
	@Override
	public void textRequestFocus() {
		requestFocus();
	}

	@Override
	public void setEnabled(boolean b) {
		component.setEnabled(b);
	}

	@Override
	public void setVisible(boolean b) {
		component.setVisible(b);
	}

	@Override
	protected void _setWidget(Object o) {
		this.component = (Component) o;
	}

	protected abstract void addActionListener(ActionListener l);

	protected abstract void addFocusListener(FocusListener l);

	/**
	 * calls the inherited implementation and resets internal state
	 */
	@Override
	protected void breakCellConnection() {
		super.breakCellConnection();
		myActionListener = null;
		myFocusListener = null;
	}

	/**
	 * Override to provide your own ActionListerner instead of
	 * {@link DefaultActionListener}
	 * 
	 * @return
	 */
	protected ActionListener createActionListener() {
		return new DefaultActionListener();
	}

	/**
	 * Override to provide your own FocusListener instead of
	 * {@link DefaultFocusListener}
	 * 
	 * @return
	 */
	protected FocusListener createFocusListener() {
		return new DefaultFocusListener();
	}

	@Override
	protected abstract String getString();

	protected abstract void removeActionListener(ActionListener l);

	protected abstract void removeFocusListener(FocusListener l);

	@Override
	protected abstract void setString(String s);

	/**
	 * Called on establishConnection. Uses {@link #createActionListener()} on
	 * first use
	 */
	@Override
	protected void startListeningForActionEvents() {
		if (myActionListener == null)
			myActionListener = createActionListener();
		addActionListener(myActionListener);
	}

	/**
	 * Called on establishConnection. Uses {@link #createFocusListener()} on
	 * first use
	 */
	@Override
	protected void startListeningForFocusEvents() {
		if (myFocusListener == null)
			myFocusListener = createFocusListener();
		addFocusListener(myFocusListener);
	}

	/**
	 * Called on breakConnection using
	 * {@link #removeActionListener(ActionListener)}
	 */
	@Override
	protected void stopListeningForActionEvents() {
		removeActionListener(myActionListener);
	}

	/**
	 * Called on breakConnection using
	 * {@link #removeFocusListener(FocusListener)}
	 */
	@Override
	protected void stopListeningForFocusEvents() {
		removeFocusListener(myFocusListener);
	}
	
	
	@Override
	protected void startListeningForKeyEvents() {
		getWidget().addKeyListener(keyListener);
	}

	@Override
	protected void stopListeningForKeyEvents() {
		getWidget().removeKeyListener(keyListener);
	}


	
}
