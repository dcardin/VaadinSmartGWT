package org.vaadin.smartgwt.client.ui.tab;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.ui.VaadinManagement;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;
import org.vaadin.smartgwt.client.ui.wrapper.TabWrapper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTabSet extends TabSet implements Paintable, VaadinManagement
{
	protected String paintableId;
	private ApplicationConnection client;
	private List<WidgetInfo> widgetInfos;
	private Element dummyDiv = null;

	@Override
	public Element getElement()
	{
		if (dummyDiv == null)
		{
			dummyDiv = DOM.createDiv();
			DOMUtil.setID(dummyDiv, getID() + "_dummy");
			RootPanel.getBodyElement().appendChild(dummyDiv);
		}
		return dummyDiv;
	}

	@Override
	public void unregister()
	{
		client.unregisterPaintable(this);
		RootPanel.getBodyElement().removeChild(dummyDiv);
		dummyDiv = null;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		addTabs(uidl, client);
	}

	private void addTabs(UIDL uidl, ApplicationConnection client)
	{
		widgetInfos = PainterHelper.paintChildren(uidl, client);

		if (widgetInfos.size() > 0)
		{
			List<Tab> tabs = new ArrayList<Tab>();

			for (WidgetInfo widgetInfo : widgetInfos)
			{
				if (widgetInfo.getWidget() instanceof TabWrapper)
				{
					Tab tab = ((TabWrapper) widgetInfo.getWidget()).getTab();

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
