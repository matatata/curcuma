package de.ceruti.curcuma.appkit.widgets.swing;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.core.MutableIndexSet;
import de.ceruti.curcuma.core.Factory;

public abstract class JListSelectionListener implements ListSelectionListener {

	@Override
	public void valueChanged(ListSelectionEvent lse) {
		if (!lse.getValueIsAdjusting()) {
			int[] selection = getSelectedIndices(lse.getSource(), getModel(
					lse.getSource()).getMinSelectionIndex(), getModel(
					lse.getSource()).getMaxSelectionIndex());

			MutableIndexSet set = Factory.mutableIndexSet();
			for (int i = 0; i < selection.length; i++) {
				int j = selection[i];
				set.add(Factory.range(j));
			}

			selectionChanged(set);
		}
	}

	protected abstract void selectionChanged(IndexSet set);

	protected int[] getSelectedIndices(Object source, int start, int stop) {
		if ((start == -1) || (stop == -1)) {
			return new int[0];
		}

		int guesses[] = new int[stop - start + 1];
		int index = 0;
		for (int i = start; i <= stop; i++) {
			if (getModel(source).isSelectedIndex(i)) {
				guesses[index++] = i;
			}
		}

		int realthing[] = new int[index];
		System.arraycopy(guesses, 0, realthing, 0, index);
		return realthing;
	}

	private ListSelectionModel getModel(Object source) {
		return (ListSelectionModel) source;
	}
}