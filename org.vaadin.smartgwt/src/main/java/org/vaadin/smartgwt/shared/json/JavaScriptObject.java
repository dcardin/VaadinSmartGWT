package org.vaadin.smartgwt.shared.json;

import java.util.Map;

import com.google.common.collect.Maps;

public class JavaScriptObject
{
	private final Map<String, JavaScriptValue<?>> members = Maps.newHashMap();

	public String getAsString(String name)
	{
		return ((JavaScriptString) members.get(name)).getValue();
	}

	public void put(String name, String value)
	{
		members.put(name, new JavaScriptString(value));
	}

	public int getAsInt(String name)
	{
		return ((JavaScriptInt) members.get(name)).getValue();
	}

	public void put(String name, int value)
	{
		members.put(name, new JavaScriptInt(value));
	}

	protected void putAllMembers(JavaScriptObject javaScriptObject)
	{
		this.members.putAll(javaScriptObject.members);
	}
}
