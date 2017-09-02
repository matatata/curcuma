package de.ceruti.curcuma.api.appkit;


public interface NSEditorEvent {
	enum Type {
		COMMIT,
		DISCARD,
		START_EDITING,
		END_EDITING
	}
	
	Type getType();

	Object getSender();
}