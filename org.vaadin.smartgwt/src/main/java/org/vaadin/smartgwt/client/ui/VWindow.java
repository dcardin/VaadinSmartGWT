package org.vaadin.smartgwt.client.ui;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VWindow extends Window implements Paintable, ClientSideHandler
{
	protected String paintableId;
	protected ApplicationConnection client;
	private final ClientSideProxy rpc = new ClientSideProxy("VWindow", this);

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		rpc.update(this, uidl, client);

		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
		PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("*items"))
		{
			String[] members = uidl.getStringArrayAttribute("*items");

			for (String c : members)
			{
				addItem((Canvas) client.getPaintable(c));
			}
		}
		
		if (uidl.hasAttribute("*members"))
		{
			// remove and unregister all members
			for (Canvas member : getMembers())
			{
				removeMember(member);
			}

			String[] members = uidl.getStringArrayAttribute("*members");

			for (String c : members)
			{
				addMember((Canvas) client.getPaintable(c));
			}
		}
		else
		{
			if (uidl.hasAttribute("*membersReplaced"))
			{
				String[] replaced = uidl.getStringArrayAttribute("*membersReplaced");

				for (int i = 0; i < replaced.length; i += 2)
				{
					Canvas removed = (Canvas) client.getPaintable(replaced[i]);
					Canvas added = (Canvas) client.getPaintable(replaced[i + 1]);

					int pos = getMemberNumber(removed);
					removeMember(removed);
					addMember(added, pos);
				}
			}
			if (uidl.hasAttribute("*membersRemoved"))
			{
				String[] removed = uidl.getStringArrayAttribute("*membersRemoved");

				for (String c : removed)
				{
					Canvas removedCanvas = (Canvas) client.getPaintable(c);
					removeMember(removedCanvas);
				}
			}
			if (uidl.hasAttribute("*membersAdded"))
			{
				String[] added = uidl.getStringArrayAttribute("*membersAdded");

				for (String c : added)
				{
					addMember((Canvas) client.getPaintable(c), 0);
				}
			}
		}
	}

	@Override
	public boolean initWidget(Object[] params)
	{
		rpc.clientInitComplete();
		return true;
	}

	@Override
	public void handleCallFromServer(String method, Object[] params)
	{
	}
}