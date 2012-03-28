package org.vaadin.smartgwt.server.core;

import static com.google.common.base.Preconditions.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;

public class PaintablePropertyPainter
{
	private final Map<String, Reference<?>> properties = Maps.newLinkedHashMap();

	public <T extends Paintable> Reference<T> addProperty(String propertyName)
	{
		checkArgument(!properties.containsKey(propertyName), propertyName + " already registered.");

		final Reference<T> reference = new Reference<T>();
		properties.put(propertyName, reference);
		return reference;
	}

	public <T extends Paintable> Reference<List<T>> addListProperty(String propertyName)
	{
		checkArgument(!properties.containsKey(propertyName), propertyName + " already registered.");

		final Reference<List<T>> reference = new Reference<List<T>>();
		reference.set(Lists.<T> newArrayList());
		properties.put(propertyName, reference);
		return reference;
	}

	public void paintContent(PaintTarget target) throws PaintException
	{
		for (Entry<String, Reference<?>> entry : properties.entrySet())
		{
			final String name = entry.getKey();
			final Object value = entry.getValue().get();

			if (value instanceof List)
			{
				paintProperty(target, name, (List<? extends Paintable>) value);
			}
			else if (value instanceof Paintable)
			{
				paintProperty(target, name, Collections.singletonList((Paintable) value));
			}
		}
	}
	
	private void paintProperty(PaintTarget target, String name, List<? extends Paintable> paintables) throws PaintException
	{
		if (!paintables.isEmpty())
		{
			target.startTag("$" + name);

			for (Paintable paintable : paintables)
			{
				paintable.paint(target);
			}

			target.endTag("$" + name);
		}
	}
}
