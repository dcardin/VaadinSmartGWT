package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VLabel extends Label implements Paintable
{
	private final Element element = DOM.createDiv();
	protected String paintableId;
	protected ApplicationConnection client;

	@Override
	public Element getElement()
	{
		return element;
	}

	public VLabel()
	{
		addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(ClickEvent event)
				{
					client.updateVariable(paintableId, "clicked", true, true);
				}
			});
	}
	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		this.paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

}
