package org.vaadin.smartgwt.client.core;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class PaintableReference implements PaintableProperty
{
	private final Set<PaintableReferenceListener> listeners = new HashSet<PaintableReferenceListener>();
	private final String name;
	private Paintable reference = null;

	public PaintableReference(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.getChildCount() == 0)
		{
			fireOnChange(reference, reference = null);
		}
		else
		{
			final UIDL elementUIDL = uidl.getChildUIDL(0);
			final Paintable paintable = client.getPaintable(elementUIDL);
			paintable.updateFromUIDL(elementUIDL, client);
			fireOnChange(reference, reference = paintable);
		}
	}

	public void addPaintableReferenceListener(PaintableReferenceListener listener)
	{
		listeners.add(listener);
	}

	private void fireOnChange(Paintable oldPaintable, Paintable newPaintable)
	{
		if (oldPaintable != newPaintable)
		{
			for (PaintableReferenceListener listener : listeners)
			{
				listener.onChange(newPaintable);
			}
		}
	}
}
