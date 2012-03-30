package org.vaadin.smartgwt.server.core;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;

public class PaintableReference<T extends Paintable> implements PaintableProperty
{
	private final String tagName;
	private T value;

	public PaintableReference(String tagName)
	{
		this.tagName = tagName;
	}

	public PaintableReference(String tagName, T value)
	{
		this.tagName = tagName;
		this.value = value;
	}

	public T get()
	{
		return value;
	}

	public void set(T value)
	{
		this.value = value;
	}

	public void paintContent(PaintTarget target) throws PaintException
	{
		target.startTag(tagName);
		target.addAttribute("type", "Reference");

		if (value != null)
		{
			value.paint(target);
		}

		target.endTag(tagName);
	}
}
