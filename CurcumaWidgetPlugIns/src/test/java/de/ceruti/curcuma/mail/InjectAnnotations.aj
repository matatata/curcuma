package de.ceruti.curcuma.mail;

import de.ceruti.curcuma.api.foundation.NSObject;
import de.ceruti.curcuma.keyvalueobserving.PostKVONotifications;

aspect InjectAnnotations {
	
	// marker interface  
	private interface KVO {}
	
	declare @type: de.ceruti.curcuma.mail..*: @NSObject;
	declare parents : de.ceruti.curcuma.mail..* implements KVO;  
	
	
	declare @method: !static public void KVO+.set*(..):
		@PostKVONotifications;
	
}
