package org.vaadin.smartgwt.client.core;

import com.vaadin.terminal.gwt.client.Paintable;

public abstract class PaintableProperty
{
	public static PaintableProperty forName(String name)
	{
		return new PaintableProperty(name)
			{
				@Override
				public void postUpdate(Paintable paintable)
				{

				}
			};
	}

	private final String name;

	public PaintableProperty(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public abstract void postUpdate(Paintable paintable);
}