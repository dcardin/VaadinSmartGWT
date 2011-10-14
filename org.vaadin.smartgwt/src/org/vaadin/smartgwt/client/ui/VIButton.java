package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VIButton extends Canvas implements Paintable
{
	public static final String CLICK_EVENT_IDENTIFIER = "click";

	protected String paintableId;
	protected ApplicationConnection client;
	private IButton button = new IButton();

	public VIButton()
	{
		super();
		button.setWidth100();
		button.setHeight100();
		setBackgroundColor("yellow");
		addChild(button);

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
//		if (client.updateComponent(this, uidl, true))
//		{
//			return;
//		}

		this.client = client;
		paintableId = uidl.getId();

		// SmartGWT Components work using absolute positioning
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);
		
		if (uidl.hasAttribute("height"))
			setHeight(uidl.getStringAttribute("height"));

		if (uidl.hasAttribute("width"))
			setWidth(uidl.getStringAttribute("width"));

		button.setPosition(Positioning.ABSOLUTE);

		PainterHelper.updateSmartGWTComponentNoDimension(button, uidl);

	}
}
