package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VButton extends Button implements Paintable
{
	public static final String CLICK_EVENT_IDENTIFIER = "click";

	protected String paintableId;
	protected ApplicationConnection client;
	private IButton button = new IButton();

	public VButton()
	{
		super();
		// addChild(button);
		// setBackgroundColor("blue");

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

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		// if (client.updateComponent(this, uidl, true))
		// {
		// return;
		// }

		this.client = client;
		paintableId = uidl.getId();

		// SmartGWT Components work using absolute positioning
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);

		// button.setPosition(Positioning.ABSOLUTE);

		PainterHelper.updateSmartGWTComponent(this, uidl);

		// setHeight(button.getHeight());
		// setWidth(button.getWidth());

	}
}
