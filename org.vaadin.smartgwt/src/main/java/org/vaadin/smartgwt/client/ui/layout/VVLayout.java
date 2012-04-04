package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VVLayout extends VLayout implements Paintable
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final Element element = DOM.createDiv();

	public VVLayout()
	{
		propertyUpdater.addPaintableListListener("members", new PaintableListListener()
			{
				@Override
				public void onAdd(Paintable[] source, Integer index, Paintable element)
				{
					if (index == null)
					{
						addMember((Canvas) element);
					}
					else
					{
						addMember((Canvas) element, index);
					}
				}

				@Override
				public void onRemove(Paintable[] source, Integer index, Paintable element)
				{
					removeMember((Canvas) element);
				}
			});
	}

	@Override
	public Element getElement()
	{
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}
}