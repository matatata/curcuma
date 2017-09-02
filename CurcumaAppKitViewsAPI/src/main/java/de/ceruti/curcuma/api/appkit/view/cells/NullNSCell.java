package de.ceruti.curcuma.api.appkit.view.cells;

import java.util.List;
import java.util.Set;

import de.ceruti.curcuma.api.core.IndexSet;
import de.ceruti.curcuma.api.keyvaluebinding.BindingInfo;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptionDescription;
import de.ceruti.curcuma.api.keyvaluebinding.BindingOptions;
import de.ceruti.curcuma.api.keyvaluecoding.KeyValueCoding;
import de.ceruti.curcuma.api.keyvaluecoding.exceptions.ValidationException;
import de.ceruti.curcuma.api.keyvalueobserving.KVOException;
import de.ceruti.curcuma.api.keyvalueobserving.KVObserver;

public class NullNSCell implements NSCell {

	protected NullNSCell(){}
	
	private static NSCell instance = new NullNSCell();
	
	public static NSCell instance() {
		return instance;
	}
	
	@Override
	public Object getBackground() {
		return null;
	}

	@Override
	public Object getCellValue() {
		return null;
	}

	@Override
	public Delegate getDelegate() {
		return null;
	}

	@Override
	public Object getDisplayValue() {
		return null;
	}

	@Override
	public Object getForeground() {
		return null;
	}

	@Override
	public Object getImplementation() {
		return null;
	}

	@Override
	public String getValidationFailureMessage() {
		return null;
	}

	@Override
	public boolean hasFocus() {
		return false;
	}

	@Override
	public boolean hasValidCellValue() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	@Override
	public boolean isVisible() {
		return false;
	}

	@Override
	public void notifyMessage(String m) {
	}

	@Override
	public void requestFocus() {
	}

	@Override
	public void setBackground(Object nativeColor) {
	}

	@Override
	public void setCellValue(Object t) {
	}

	@Override
	public void setDelegate(Delegate d) {
	}

	@Override
	public void setDisplayValue(Object displayValue) {
	}

	@Override
	public void setEditable(boolean b) {
	}

	@Override
	public void setEnabled(boolean b) {
	}

	@Override
	public void setForeground(Object nativeColor) {
	}

	@Override
	public void setVisible(boolean b) {
	}

	@Override
	public boolean updateCellValue() {
		return false;
	}

	@Override
	public void updateDisplayValue() {
	}

	@Override
	public Object validateCellValue(Object newCellVal)
			throws ValidationException {
		return null;
	}

	@Override
	public Object validateDisplayValue(Object newDisplayVal)
			throws ValidationException {
		return null;
	}

	@Override
	public Object getValueForKey(String key) {
		return null;
	}

	@Override
	public Object getValueForKeyPath(String keypath) {
		return null;
	}

	@Override
	public List mutableArrayValueForKey(String key) {
		return null;
	}

	@Override
	public List mutableArrayValueForKeyPath(String keypath) {
		return null;
	}

	@Override
	public Set mutableSetValueForKey(String key) {
		return null;
	}
	
	@Override
	public Set mutableSetValueForKeyPath(String keypath) {
		return null;
	}
	
	@Override
	public void setValueForKey(Object value, Class<?> type, String key) {
	}

	@Override
	public void setValueForKey(Object value, String key) {
	}

	@Override
	public void setValueForKeyPath(Object value, Class<?> type, String keypath) {
	}

	@Override
	public void setValueForKeyPath(Object value, String keypath) {
	}

	@Override
	public Object validateValueForKey(Object value, String key)
			throws ValidationException {
		return null;
	}

	@Override
	public Object validateValueForKeyPath(Object value, String keyPath)
			throws ValidationException {
		return null;
	}

	@Override
	public KeyValueCoding createCompliantObjectForKey(Object element,
			String key, boolean isWrite) {
		return null;
	}

	@Override
	public Object getValueForUndefinedKey(String key) {
		return null;
	}

	@Override
	public void setNullValueForKey(String key) {
	}

	@Override
	public void setValueForUndefinedKey(Object value, Class<?> type, String key) {
	}

	@Override
	public void addObserver(KVObserver observer, String keyPath,
			Object context, int options) throws KVOException {
	}

	@Override
	public void didChangeValueForKey(String key) {
	}

	@Override
	public void didChangeValueForKeyPath(String keyPath) {
	}

	@Override
	public void didChangeValuesAtIndexesForKey(int KVOChangeKind,
			IndexSet indexes, String key) {
	}

	@Override
	public void removeObserver(KVObserver observer, String keyPath) {
	}

	@Override
	public void willChangeValueForKey(String key) {
	}

	@Override
	public void willChangeValueForKeyPath(String keyPath) {
	}

	@Override
	public void willChangeValuesAtIndexesForKey(int KVOChangeKind,
			IndexSet indexes, String key) {
	}

	@Override
	public void addBindingOptionDescriptionForBinding(String binding,
			BindingOptionDescription desc) {
	}

	@Override
	public void bind(String binding, Object observable, String withKeyPath,
			BindingOptions options) {
	}

	@Override
	public Set<String> exposedBindings() {
		return null;
	}

	@Override
	public BindingOptionDescription getBindingOptionDescription(String binding,
			String optionKey) {
		return null;
	}

	@Override
	public Set<String> getBindingOptionKeysForBinding(String binding) {
		return null;
	}

	@Override
	public BindingInfo getInfoForBinding(String binding) {
		return null;
	}

	@Override
	public boolean isBound(String binding) {
		return false;
	}

	@Override
	public void removeBindingOptionDescriptionForBinding(String binding,
			String key) {
	}

	@Override
	public void unbind(String binding) {
	}

	@Override
	public void setValidityStatus(boolean isValid, String reason) {
	}

}
