package org.vaadin.smartgwt.client.ui.tab;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTabSet extends TabSet implements Paintable
{
	protected String paintableId;
	private ApplicationConnection client;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
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
				Tab tab = ((Wrapper) client.getPaintable(member)).unwrap();
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
					Tab oldTab = ((Wrapper) client.getPaintable(replaced[i])).unwrap();
					Tab newTab = ((Wrapper) client.getPaintable(replaced[i + 1])).unwrap();

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
					Tab tab = ((Wrapper) client.getPaintable(member)).unwrap();
					removeTab(tab);
				}
			}
			if (uidl.hasAttribute("*membersAdded"))
			{
				String[] added = uidl.getStringArrayAttribute("*membersAdded");

				for (String member : added)
				{
					Tab tab = ((Wrapper) client.getPaintable(member)).unwrap();
					addTab(tab);
				}
			}
		}
	}
}
