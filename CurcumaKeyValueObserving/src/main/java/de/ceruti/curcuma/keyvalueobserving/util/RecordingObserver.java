/**
 * 
 */
package de.ceruti.curcuma.keyvalueobserving.util;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.ceruti.curcuma.api.keyvalueobserving.KVOEvent;
import de.ceruti.curcuma.api.keyvalueobserving.KeyValueObserving;

public class RecordingObserver extends LoggingObserver {

	private List<KVOEvent> evts;

	public RecordingObserver(){
		this("untitled",System.out,new ArrayList<KVOEvent>());
	}

	
	public RecordingObserver(String name){
		this(name,System.out,new ArrayList<KVOEvent>());
	}
	
	public RecordingObserver(String name,PrintStream writer,List<KVOEvent> destination) {
		super(name,writer);
		evts = destination;
	}
	
	public RecordingObserver(String name,List<KVOEvent> destination) {
		this(name,System.out,destination);
	}
	
	@Override
	public void observeValue(String keypath, KeyValueObserving object,
			KVOEvent change, Object context) {
		evts.add(change);
		super.observeValue(keypath, object, change, context);
	}

	public List<KVOEvent> getEvts() {
		return Collections.unmodifiableList(evts);
	}

	public static String recordingToString(List<KVOEvent> recordings) {
		StringBuilder sb=new StringBuilder();
		for(KVOEvent e : recordings) {
			sb.append(e).append("\r\n");
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return recordingToString(evts);
	}
	
	public void clear(){
		evts.clear();
	}
	
}