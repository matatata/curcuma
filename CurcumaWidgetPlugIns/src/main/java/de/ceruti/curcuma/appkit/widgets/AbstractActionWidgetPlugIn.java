package de.ceruti.curcuma.appkit.widgets;

import de.ceruti.curcuma.appkit.view.cells.ActionControlWidgetPlugin;

public abstract class AbstractActionWidgetPlugIn extends AbstractWidgetPlugIn implements ActionControlWidgetPlugin {

	public AbstractActionWidgetPlugIn() {
		super();
	}

	@Override
	public ActionControlWidgetPlugin.Delegate getWidgetAssociation() {
		Object assoc = super.getWidgetAssociation();
		if (assoc instanceof ActionControlWidgetPlugin.Delegate)
			return (ActionControlWidgetPlugin.Delegate) assoc;
		return ActionControlWidgetPlugin.Delegate.Dummy.INSTANCE;
	}
	
	/**
	 * Asks the delegate whether pluginDidPerformAction should be called by using
	 * pluginShouldPerformAction() and calls it. 
	 * Will do nothing if {@link #notifyAssociationEnabled()} returns false.
	 */
	protected void actionPerformed() {
		if (!notifyAssociationEnabled())
			return;

		if (getWidgetAssociation().pluginShouldPerformAction(this)) {
			getWidgetAssociation().pluginDidPerformAction(this);
		}
	}

}
