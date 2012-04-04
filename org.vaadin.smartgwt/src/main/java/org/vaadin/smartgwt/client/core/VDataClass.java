package org.vaadin.smartgwt.client.core;

import com.smartgwt.client.core.DataClass;
import com.vaadin.terminal.gwt.client.ApplicationConnection;

public abstract class VDataClass<T extends DataClass> extends VJSObject<T>
{
	public static <T extends DataClass> VDataClass<T> getVDataClass(ApplicationConnection client, T dataClass)
	{
		return (VDataClass<T>) client.getPaintable(dataClass.getAttributeAsString(ATTRIBUTE_PID));
	}

	public static <T extends DataClass> T getDataClass(ApplicationConnection client, String pid)
	{
		return ((VDataClass<T>) client.getPaintable(pid)).getJSObject();
	}

	protected VDataClass(T dataClass)
	{
		super(dataClass);
	}

	@Override
	protected void setJSObjectAttribute(String name, String value)
	{
		getJSObject().setAttribute(name, value);
	}
}
