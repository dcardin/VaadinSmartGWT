package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.layout.HLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VHLayout extends HLayout implements Paintable
{
	protected String paintableId;
	ApplicationConnection client;

	public VHLayout()
	{
		super();
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (client.updateComponent(this, uidl, true))
		{
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		for (Widget widget : PainterHelper.paintChildren(uidl, client))
		{
			addMember(widget);
		}

		reflowNow();

		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);

	}

}
