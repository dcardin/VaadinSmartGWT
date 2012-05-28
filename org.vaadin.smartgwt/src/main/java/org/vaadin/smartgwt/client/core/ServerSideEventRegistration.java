package org.vaadin.smartgwt.client.core;

import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class ServerSideEventRegistration {
	private final String uidlAttribute;
	private HandlerRegistration registration;

	public ServerSideEventRegistration(String uidlAttribute) {
		this.uidlAttribute = uidlAttribute;
	}

	public void updateFromUIDL(UIDL uidl) {
		if (uidl.hasAttribute(uidlAttribute) && registration == null) {
			registration = registerHandler();
		} else if (!uidl.hasAttribute(uidlAttribute) && registration != null) {
			registration.removeHandler();
			registration = null;
		}
	}

	protected abstract HandlerRegistration registerHandler();
}