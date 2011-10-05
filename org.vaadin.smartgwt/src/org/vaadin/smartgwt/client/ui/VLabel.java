package org.vaadin.smartgwt.client.ui;

import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Label;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VLabel extends Label implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;

	public VLabel()
	{
		super();
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		// SmartGWT Components work using absolute positioning
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);
		
		PainterHelper.updateSmartGWTComponent(this, uidl);
		
		if (uidl.hasAttribute("contents"))
			setContents(uidl.getStringAttribute("contents"));
	}

}
