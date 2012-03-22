package org.vaadin.smartgwt.client.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class PaintablePropertyUpdater
{
	private final List<PaintableProperty> paintableProperties;

	public PaintablePropertyUpdater(List<PaintableProperty> paintableProperties)
	{
		this.paintableProperties = new ArrayList<PaintableProperty>(paintableProperties);
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		for (UIDL propertyUIDL : filterUIDLPropertyChildren(uidl.getChildIterator()))
		{
			final List<Paintable> paintables = updateChildrenFromUIDL(client, propertyUIDL);

			if (!paintables.isEmpty())
			{
				final PaintableProperty paintableProperty = findPaintableProperty(propertyUIDL.getTag());

				if (paintableProperty != null)
				{
					paintableProperty.postUpdate(paintables.get(0));
				}
			}
		}
	}

	private PaintableProperty findPaintableProperty(String propertyName)
	{
		for (PaintableProperty property : paintableProperties)
		{
			if (property.getName().equals(propertyName.substring(1)))
			{
				return property;
			}
		}

		return null;
	}

	private static List<Paintable> updateChildrenFromUIDL(ApplicationConnection client, UIDL propertyUIDL)
	{
		final List<Paintable> paintables = new ArrayList<Paintable>();

		for (Iterator<Object> iterator = propertyUIDL.getChildIterator(); iterator.hasNext();)
		{
			final UIDL paintableUIDL = (UIDL) iterator.next();
			final Paintable paintable = client.getPaintable(paintableUIDL);
			paintable.updateFromUIDL(paintableUIDL, client);
			paintables.add(paintable);
		}

		return paintables;
	}

	private static List<UIDL> filterUIDLPropertyChildren(Iterator<Object> childrenIterator)
	{
		final List<UIDL> children = new ArrayList<UIDL>();

		while (childrenIterator.hasNext())
		{
			final Object next = childrenIterator.next();

			if (next instanceof UIDL && ((UIDL) next).getTag().startsWith("$"))
			{
				children.add((UIDL) next);
			}
		}

		return children;
	}
}