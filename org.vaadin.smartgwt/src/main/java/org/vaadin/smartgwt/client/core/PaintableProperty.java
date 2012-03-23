package org.vaadin.smartgwt.client.core;

import com.vaadin.terminal.gwt.client.Paintable;

public abstract class PaintableProperty
{
	private final String name;

	public PaintableProperty(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public abstract void postUpdate(Paintable[] paintables);
}