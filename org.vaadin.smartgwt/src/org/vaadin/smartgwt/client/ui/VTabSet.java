package org.vaadin.smartgwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import org.vaadin.smartgwt.client.ui.wrapper.TabWrapper;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTabSet extends TabSet implements Paintable
{
	protected String paintableId;
	private ApplicationConnection client;

	public VTabSet()
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

		addTabs(uidl, client);
		
		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);
	}


	private void addTabs(UIDL uidl, ApplicationConnection client)
	{
		List<Widget> widgets = PainterHelper.paintChildren(uidl, client);

		if (widgets.size() > 0)
		{
			List<Tab> tabs = new ArrayList<Tab>();

			for (Widget widget : widgets)
			{
				if (widget instanceof TabWrapper)
				{
					Tab tab = ((TabWrapper) widget).getTab();

					if (tab != null) 
						tabs.add(tab);
				}
			}
			
			if (tabs.size() > 0)
			{
				Tab[] tabsArr = new Tab[0];
				tabsArr = tabs.toArray(tabsArr);
				
				setTabs(tabsArr);
			}
		}
	}

}
