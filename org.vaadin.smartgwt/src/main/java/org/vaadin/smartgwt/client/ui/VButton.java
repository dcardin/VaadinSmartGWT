package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VButton extends Button implements Paintable, VaadinManagement
{
	public static final String CLICK_EVENT_IDENTIFIER = "click";

	protected String paintableId;
	protected ApplicationConnection client;
	private Element dummyDiv = null;

	@Override
	public void unregister()
	{
		client.unregisterPaintable(this);
		RootPanel.getBodyElement().removeChild(dummyDiv);
		dummyDiv = null;
	}

	@Override
	public Element getElement()
	{
		if (dummyDiv == null)
		{
			dummyDiv = DOM.createDiv();
			DOMUtil.setID(dummyDiv, getID() + "_dummy");
			RootPanel.getBodyElement().appendChild(dummyDiv);
		}
		return dummyDiv;
	}

	public VButton()
	{
		super();

		addClickHandler(new ClickHandler()
			{
				@Override
				public void onClick(com.smartgwt.client.widgets.events.ClickEvent event)
				{
					String button = "left click";
					client.updateVariable(paintableId, CLICK_EVENT_IDENTIFIER, button, true);
				}
			});
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);
	}

}
