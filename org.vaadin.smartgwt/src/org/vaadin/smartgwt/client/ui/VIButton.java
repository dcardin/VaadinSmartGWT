package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.ui.Composite;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
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
		setAutoWidth();
		setAutoHeight();
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
		if (client.updateComponent(this, uidl, true))
		{
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		for (String att : uidl.getAttributeNames())
		{
			if (att.startsWith("*"))
			{
				String name = att.substring(1);
				setProperty(button, name, uidl.getStringAttribute(att));
			}
		}

		// SmartGWT Components work using absolute positioning
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);

		button.setPosition(Positioning.ABSOLUTE);

		//PainterHelper.updateSmartGWTComponent(button, uidl);

	}

    public native void setProperty(Object obj, String property, String value)/*-{
    	var widget = obj.@com.smartgwt.client.widgets.BaseWidget::getJsObj()();
    	widget.setProperty(property, value);
	}-*/;
}
