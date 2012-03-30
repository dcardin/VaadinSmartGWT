package org.vaadin.smartgwt.server.core;

import java.util.Set;

import com.google.common.collect.Sets;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;

public class PaintablePropertyPainter
{
	private final Set<PaintableProperty> properties = Sets.newLinkedHashSet();

	public <T extends Paintable> PaintableReference<T> addProperty(String propertyName)
	{
		final PaintableReference<T> reference = new PaintableReference<T>("$" + propertyName);
		properties.add(reference);
		return reference;
	}

	public <T extends Paintable> PaintableList<T> addPaintableList(String propertyName)
	{
		final PaintableList<T> paintables = new PaintableList<T>("$" + propertyName);
		properties.add(paintables);
		return paintables;
	}

	public void paintContent(PaintTarget target) throws PaintException
	{
		for (PaintableProperty property : properties)
		{
			property.paintContent(target);
		}
	}
}
