package org.vaadin.smartgwt.client.core;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public interface PaintableProperty
{
	String getName();

	void updateFromUIDL(UIDL uidl, ApplicationConnection client);
}