package org.vaadin.smartgwt.server.core;

/**
 * Holds a registration entry when an object is registered to another.  Used to unregister. 
 */
public interface RegistrationEntry {
	/**
	 * Verifies if the object entry is still associated with the registration object.
	 * 
	 * @return the registration state.
	 */
	boolean isRegistered();

	/**
	 * Unregisters the object entry associated with the registration object if the association is still present.  Else, the call does nothing.  
	 */
	void unregister();
}