package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.StatefulCanvas;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VImg extends StatefulCanvas implements Paintable
{
	public static final String CLICK_EVENT_IDENTIFIER = "click";

	private final Element element = DOM.createDiv();
	protected String paintableId;
	protected ApplicationConnection client;

	@Override
	public Element getElement()
	{
		return element;
	}

	public VImg()
	{
		super();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

}
