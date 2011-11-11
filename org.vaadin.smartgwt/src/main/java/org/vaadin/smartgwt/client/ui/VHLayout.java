package org.vaadin.smartgwt.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.Canvas;
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
		// SmartGWT Components work using absolute positioning
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);

		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		// materialize all passed children
		List<Widget> widgets = PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("children-painted"))
		{
			// full repaint
			removeMembers(getMembers());
			for (Widget widget : widgets)
			{
				addMember(widget);
			}
		}
		else
		{
			if (uidl.hasAttribute("replaced"))
			{
				String[] replaced = uidl.getStringArrayAttribute("replaced");
				for (int i = 0; i < replaced.length; i += 2)
				{
					Canvas removed = (Canvas) client.getPaintable(replaced[i]);
					Canvas added = (Canvas) client.getPaintable(replaced[i + 1]);
					int pos = getMemberNumber(removed);
					removeMember(removed);
					addMember(added, pos);
				}
			}
			if (uidl.hasAttribute("removed"))
			{
				String[] removed = uidl.getStringArrayAttribute("removed");
				for (String c : removed)
				{
					removeMember((Canvas) client.getPaintable(c));
				}
			}
			if (uidl.hasAttribute("added"))
			{
				String[] added = uidl.getStringArrayAttribute("added");
				for (String c : added)
				{
					addMember((Canvas) client.getPaintable(c));
				}
			}
		}
	}
}
