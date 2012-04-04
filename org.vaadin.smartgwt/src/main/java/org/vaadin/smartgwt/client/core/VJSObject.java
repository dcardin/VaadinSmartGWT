package org.vaadin.smartgwt.client.core;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VJSObject<T> extends Widget implements Paintable
{
	protected static final String ATTRIBUTE_PID = "pid";

	private final Element element = DOM.createDiv();
	private final T object;

	protected VJSObject(T object)
	{
		this.object = object;
	}

	@Override
	public final Element getElement()
	{
		return element;
	}

	@Override
	public final void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		setStringAttribute(ATTRIBUTE_PID, uidl.getId());
		preAttributeUpdateFromUIDL(uidl, client);
		updateJSObjectAttributes(uidl, client);
		postAttributeUpdateFromUIDL(uidl, client);
	}

	public final T getJSObject()
	{
		return object;
	}

	protected abstract void setStringAttribute(String name, String value);

	protected abstract void updateJSObjectAttributes(UIDL uidl, ApplicationConnection client);

	/**
	 * Called before updating the dynamic attributes. Referenced paintables should be painted at this point to prevent dynamic update to refer to a
	 * non-registered PID.
	 */
	protected abstract void preAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client);

	/**
	 * Called after dynamic attributes have been updated. Behavior dependent on dynamic attribute values should be done at this point.
	 */
	protected abstract void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client);
}
