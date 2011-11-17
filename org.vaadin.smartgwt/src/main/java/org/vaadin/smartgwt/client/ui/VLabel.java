package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Label;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VLabel extends Label implements Paintable, VaadinManagement
{
	protected String paintableId;
	protected ApplicationConnection client;
	private Element dummyDiv = null;

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

	@Override
	public void unregister()
	{
		client.unregisterPaintable(this);
		RootPanel.getBodyElement().removeChild(dummyDiv);
		dummyDiv = null;
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		PainterHelper.updateSmartGWTComponent(this, uidl);
	}

}
