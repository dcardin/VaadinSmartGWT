package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSectionStack extends SectionStack implements Paintable
{
	private final Element element = DOM.createDiv();
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VSectionStack()
	{
		propertyUpdater.addProperty(new PaintableProperty("sections")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					for (Paintable paintable : paintables)
					{
						addSection(((VSectionStackSection) paintable).getJSObject());
					}
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