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
		for (PaintableProperty paintableProperty : paintableProperties)
		{
			final List<Paintable> paintables = new ArrayList<Paintable>();
			final UIDL childUIDL = uidl.getChildByTagName(paintableProperty.getName());

			if (childUIDL != null)
			{
				for (Iterator<Object> iterator = childUIDL.getChildIterator(); iterator.hasNext();)
				{
					final UIDL paintableUIDL = (UIDL) iterator.next();
					final Paintable paintable = client.getPaintable(paintableUIDL);
					paintable.updateFromUIDL(paintableUIDL, client);
					paintables.add(paintable);
				}
			}

			if (!paintables.isEmpty())
			{
				paintableProperty.postUpdate(paintables.get(0));
			}
		}
	}
}