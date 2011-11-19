package org.vaadin.smartgwt.client.ui.tab;

import java.util.List;

import org.vaadin.smartgwt.client.ui.VaadinManagement;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;
import org.vaadin.smartgwt.client.ui.wrapper.TabWrapper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.tab.Tab;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTab extends Label implements Paintable, TabWrapper, VaadinManagement
{
	protected String paintableId;
	protected ApplicationConnection client;
	private final Tab tab;
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

	public VTab()
	{
		super();
		tab = new Tab();
		setSize("1px", "1px");
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		List<WidgetInfo> widgetInfos = PainterHelper.paintChildren(uidl, client);

		// Tabs should only have ONE child...
		if (widgetInfos.size() != 1)
			throw new IllegalStateException("Tabs should only have one child, representing the pane");

		Widget pane = widgetInfos.get(0).getWidget();

		if (pane instanceof Canvas)
		{
			tab.setPane((Canvas) pane);
		}

		if (uidl.hasAttribute("*title"))
			tab.setTitle(uidl.getStringAttribute("*title").substring(1));
	}

	@Override
	public Tab getTab()
	{
		return tab;
	}
}
