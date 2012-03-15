package org.vaadin.smartgwt.client.core;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VJSObject<T> extends Widget implements Paintable
{
	protected static final String ATTRIBUTE_PID = "pid";

	private final T object;
	private ApplicationConnection client;
	private String pid;

	protected VJSObject(T object)
	{
		this.object = object;
	}

	@Override
	public final Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public final void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (pid == null)
		{
			setJSObjectAttribute("pid", pid = uidl.getId());
		}

		if (this.client == null)
		{
			this.client = client;
		}

		updateJSObjectAttributes(uidl);
		updateFromUIDL(uidl);
	}

	public final T getJSObject()
	{
		return object;
	}

	protected final String getPID()
	{
		return pid;
	}

	protected final ApplicationConnection getClient()
	{
		return client;
	}

	protected abstract void setJSObjectAttribute(String name, String value);

	protected abstract void updateJSObjectAttributes(UIDL uidl);

	protected abstract void updateFromUIDL(UIDL uidl);
}
