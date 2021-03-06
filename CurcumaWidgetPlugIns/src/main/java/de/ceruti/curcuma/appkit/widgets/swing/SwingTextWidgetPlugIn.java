package de.ceruti.curcuma.appkit.widgets.swing;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;

import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.JTextComponent;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Logger;

import de.ceruti.curcuma.appkit.widgets.awt.AbstractAWTTextWidgetPlugIn;

public class SwingTextWidgetPlugIn extends AbstractAWTTextWidgetPlugIn {
	private static Logger logger = org.apache.logging.log4j.LogManager.getLogger(SwingTextWidgetPlugIn.class);

	private DefaultInputVerifier myInputVerifier = new DefaultInputVerifier();

	private InputVerifier oldVerifier;
	private TipInterface balloonTipSupport =
//	= new MBoxTooltip();
		//new TooltipStuff();
		new BalloonTipSupport();

	
	public SwingTextWidgetPlugIn() {
		super();
	}

	protected JComponent getJComponent() {
		return (JComponent) getWidget();
	}
	
	@Override
	protected void _setViewWidget(Object w) {
	}

	@Override
	public JComponent getViewWidget() {
		return getJComponent();
	}


	@Override
	public void notifyMessage(String m) {
		balloonTipSupport.notifyMessage(m);
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

	/**
	 * delegates to Implementation
	 */
	@Override
	protected final void addActionListener(ActionListener l) {
		getImpl().addActionListener(l);
	}

	@Override
	protected void addFocusListener(FocusListener l) {
		getJComponent().addFocusListener(l);
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	protected final String getString() {
		return getImpl().getString();
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	protected final void removeActionListener(ActionListener l) {
		getImpl().removeActionListener(l);
	}

	@Override
	protected void removeFocusListener(FocusListener l) {
		getJComponent().removeFocusListener(l);
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	protected final void setString(String s) {
		getImpl().setString(s);
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	protected final void startListeningForTextEvents() {
		getImpl().startListeningForTextEvents();
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	protected final void stopListeningForTextEvents() {
		getImpl().stopListeningForTextEvents();
	}

	@Override
	protected void breakCellConnection() {
		balloonTipSupport.disconnect(getJComponent());
		removeInputVerifier();
		getImpl().breakCellConnection();
		super.breakCellConnection();
		setImpl(null);
	}

	@Override
	protected void establishCellConnection() {
		setImpl(createImpl(getJComponent()));
		super.establishCellConnection();
		addInputVerifier();
		getImpl().establishCellConnection();
		balloonTipSupport.connect(getJComponent());
	}

	private void addInputVerifier() {
		oldVerifier = getJComponent().getInputVerifier();
		getJComponent().setInputVerifier(myInputVerifier);
	}

	private void removeInputVerifier() {
		getJComponent().setInputVerifier(oldVerifier);
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	public final void clearSelection() {
		getImpl().clearSelection();
	}

	/**
	 * delegates to Implementation
	 */
	@Override
	public final void selectAll() {
		getImpl().selectAll();
	}


	protected class DefaultInputVerifier extends InputVerifier {

		@Override
		public boolean verify(JComponent input) {
			// Ask delegate?
			return true;
		}

		@Override
		public boolean shouldYieldFocus(JComponent input) {

			return getWidgetAssociation().textShouldEndEditing(
					SwingTextWidgetPlugIn.this);
		}
	}


	protected Implementation createImpl(JComponent widget){
		if(widget instanceof JTextComponent)
			return new JTextComponentImplementation((JTextComponent)widget);
		return null;
	}

	protected interface Implementation {
		void selectAll();

		void establishCellConnection();

		void breakCellConnection();

		void stopListeningForTextEvents();

		void startListeningForTextEvents();

		void addActionListener(ActionListener l);

		String getString();

		void removeActionListener(ActionListener l);

		void setString(String s);

		void clearSelection();
		
		class Dummy implements Implementation {

			@Override
			public void addActionListener(ActionListener l) {
			}

			@Override
			public void breakCellConnection() {
			}

			@Override
			public void clearSelection() {
			}

			@Override
			public void establishCellConnection() {
			}

			@Override
			public String getString() {
				return "";
			}

			@Override
			public void removeActionListener(ActionListener l) {
			}

			@Override
			public void selectAll() {
			}

			@Override
			public void setString(String s) {
			}

			@Override
			public void startListeningForTextEvents() {
			}

			@Override
			public void stopListeningForTextEvents() {
			}
			
			public static Implementation INSTANCE = new Dummy();
		}
	}

	private Implementation impl;
	

	protected void setImpl(Implementation impl) {
		this.impl = impl;
	}

	protected Implementation getImpl() {
		return impl == null ? Implementation.Dummy.INSTANCE : impl;
	}

	class JTextComponentImplementation implements
			SwingTextWidgetPlugIn.Implementation {
		private DocumentFilter oldFilter;
		private DefaultDocumentFilter myDocumentFilter = new DefaultDocumentFilter();
		private JTextComponent widget;
		
		protected JTextComponent getWidget() {
			return widget;
		}

		public JTextComponentImplementation(JTextComponent widget) {
			this.widget = widget;
		}

		@Override
		public void addActionListener(ActionListener l) {
			if (getWidget() instanceof JTextField)
				((JTextField) getWidget()).addActionListener(l);
		}

		@Override
		public String getString() {
			return getWidget().getText();
		}

		@Override
		public void removeActionListener(ActionListener l) {
			if (getWidget() instanceof JTextField)
				((JTextField) getWidget()).removeActionListener(l);
		}

		@Override
		public void setString(String s) {
			getWidget().setText(s);
		}

		@Override
		public void startListeningForTextEvents() {

			DocumentFilter currentFilter = ((AbstractDocument) getWidget().getDocument())
					.getDocumentFilter();

			if(currentFilter!=null && currentFilter != myDocumentFilter)
				oldFilter = currentFilter;
			
			((AbstractDocument) getWidget().getDocument())
					.setDocumentFilter(myDocumentFilter);

		}

		@Override
		public void stopListeningForTextEvents() {
			((AbstractDocument) getWidget().getDocument())
					.setDocumentFilter(oldFilter);

		}

		@Override
		public void clearSelection() {
			getWidget().select(0, 0);
		}

		@Override
		public void selectAll() {
			getWidget().selectAll();
		}

		protected class DefaultDocumentFilter extends DocumentFilter {

			@Override
			public void insertString(FilterBypass arg0, int arg1, String arg2,
					AttributeSet arg3) throws BadLocationException {
				if(!notifyAssociationEnabled())
					super.insertString(arg0, arg1, arg2, arg3);
				else if (isEditingState()
						|| getWidgetAssociation()
								.textShouldBeginEditing(SwingTextWidgetPlugIn.this)) {
					if (oldFilter != null)
						oldFilter.insertString(arg0, arg1, arg2, arg3);
					else
						super.insertString(arg0, arg1, arg2, arg3);
				}
				textHasChanged();
			}

			@Override
			public void remove(FilterBypass arg0, int arg1, int arg2)
					throws BadLocationException {
				if(!notifyAssociationEnabled())
					super.remove(arg0, arg1, arg2);
				else if (isEditingState()
						|| getWidgetAssociation()
								.textShouldBeginEditing(SwingTextWidgetPlugIn.this)) {
					if (oldFilter != null)
						oldFilter.remove(arg0, arg1, arg2);
					else
						super.remove(arg0, arg1, arg2);
				}
				textHasChanged();
			}

			@Override
			public void replace(FilterBypass arg0, int arg1, int arg2,
					String arg3, AttributeSet arg4) throws BadLocationException {
				if(!notifyAssociationEnabled())
					super.replace(arg0, arg1, arg2, arg3, arg4);
				else if (isEditingState()
						|| getWidgetAssociation()
								.textShouldBeginEditing(SwingTextWidgetPlugIn.this)) {
					if (oldFilter != null)
						oldFilter.replace(arg0, arg1, arg2, arg3, arg4);
					else
						super.replace(arg0, arg1, arg2, arg3, arg4);
				}
				textHasChanged();
			}
		}

		@Override
		public void breakCellConnection() {
		}

		@Override
		public void establishCellConnection() {
		}

	}

}
