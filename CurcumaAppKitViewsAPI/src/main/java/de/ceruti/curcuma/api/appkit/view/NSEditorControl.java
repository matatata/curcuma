package de.ceruti.curcuma.api.appkit.view;

import de.ceruti.curcuma.api.appkit.NSEditor;

public interface NSEditorControl extends NSEditor, NSControl {
	boolean isRevertOnCommitFailure();
	void setRevertOnCommitFailure(boolean isRevertOnCommitFailure);
}
