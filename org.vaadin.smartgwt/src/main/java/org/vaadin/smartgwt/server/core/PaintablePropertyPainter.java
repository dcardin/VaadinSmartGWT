package org.vaadin.smartgwt.server.core;

import java.util.Set;

import com.google.common.collect.Sets;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;
import com.vaadin.ui.Component;

public class PaintablePropertyPainter
{
	private final Set<PaintableProperty> properties = Sets.newLinkedHashSet();

	public <T extends Paintable> PaintableReference<T> addProperty(String propertyName)
	{
		final PaintableReference<T> reference = new PaintableReference<T>("$" + propertyName);
		properties.add(reference);
		return reference;
	}

	public <T extends Component> ComponentList<T> addPaintableList(String propertyName)
	{
		final ComponentList<T> paintables = new ComponentList<T>("$" + propertyName);
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
