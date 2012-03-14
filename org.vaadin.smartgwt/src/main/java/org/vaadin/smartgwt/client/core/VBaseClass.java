package org.vaadin.smartgwt.client.core;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.core.BaseClass;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VBaseClass<T extends BaseClass> extends VJSObject<T>
{
	public static <T extends BaseClass> VBaseClass<T> getVBaseClass(ApplicationConnection client, T baseClass)
	{
		return (VBaseClass<T>) client.getPaintable(baseClass.getAttributeAsString(ATTRIBUTE_PID));
	}

	public static <T extends BaseClass> T getBaseClass(ApplicationConnection client, String pid)
	{
		return ((VBaseClass<T>) client.getPaintable(pid)).getJSObject();
	}

	protected VBaseClass(T baseClass)
	{
		super(baseClass);
	}

	@Override
	protected void setJSObjectAttribute(String name, String value)
	{
		getJSObject().setAttribute(name, value, true);
	}

	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.updateBaseClass(getClient(), getJSObject(), uidl);
	}
}
