package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VVLayout extends VLayout implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;

	public VVLayout()
	{
		super();
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
//		if (client.updateComponent(this, uidl, true))
//		{
//			return;
//		}

		// SmartGWT Components work using absolute positioning
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);

		setPosition(Positioning.RELATIVE);
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		for (Widget widget : PainterHelper.paintChildren(uidl, client))
		{
			addMember(widget);
		}
	}
}