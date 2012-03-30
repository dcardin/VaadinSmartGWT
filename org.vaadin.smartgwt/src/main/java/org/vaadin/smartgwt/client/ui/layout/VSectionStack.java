package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSectionStack extends SectionStack implements Paintable
{
	private final Element element = DOM.createDiv();
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VSectionStack()
	{
		propertyUpdater.addPaintableListListener("sections", new PaintableListListener()
			{
				@Override
				public void onAdd(Paintable[] source, Integer index, Paintable element)
				{
					final SectionStackSection section = ((VSectionStackSection) element).getJSObject();

					if (index == null)
					{
						addSection(section, index);
					}
					else
					{
						addSection(section);
					}
				}

				@Override
				public void onRemove(Paintable[] source, Integer index, Paintable element)
				{
					removeSection(index);
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