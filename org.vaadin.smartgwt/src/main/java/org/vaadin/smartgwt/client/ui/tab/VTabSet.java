package org.vaadin.smartgwt.client.ui.tab;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.rpc.shared.Method;
import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTabSet extends TabSet implements Paintable, ClientSideHandler
{
	protected String paintableId;
	private ApplicationConnection client;
	private final ClientSideProxy rpc = new ClientSideProxy("VTabSet", this);

	public VTabSet()
	{
		super();
		rpc.register("selectTab", new Method()
			{
				@Override
				public void invoke(final String methodName, final Object[] data)
				{
					selectTab((Integer) data[0]);
				}
			});
	}

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

		addTabs(uidl, client);
	}

	private void addTabs(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*members"))
		{
			// remove and unregister all members
			for (Tab tab : getTabs())
			{
				removeTab(tab);
			}

			String[] members = uidl.getStringArrayAttribute("*members");

			for (String member : members)
			{
				Tab tab = VDataClass.getDataClass(client, member);
				addTab(tab);
			}
		}
		else
		{
			if (uidl.hasAttribute("*membersReplaced"))
			{
				String[] replaced = uidl.getStringArrayAttribute("*membersReplaced");

				for (int i = 0; i < replaced.length; i += 2)
				{
					Tab oldTab = VDataClass.getDataClass(client, replaced[i]);
					Tab newTab = VDataClass.getDataClass(client, replaced[i + 1]);

					int pos = getTabNumber(oldTab.getID());
					removeTab(oldTab);

					addTab(newTab, pos);
				}
			}
			if (uidl.hasAttribute("*membersRemoved"))
			{
				String[] removed = uidl.getStringArrayAttribute("*membersRemoved");

				for (String member : removed)
				{
					Tab tab = VDataClass.getDataClass(client, member);
					removeTab(tab);
				}
			}
			if (uidl.hasAttribute("*membersAdded"))
			{
				String[] added = uidl.getStringArrayAttribute("*membersAdded");

				for (String member : added)
				{
					Tab tab = VDataClass.getDataClass(client, member);
					addTab(tab);
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
		System.out.println("method call: " + method);
	}

}
