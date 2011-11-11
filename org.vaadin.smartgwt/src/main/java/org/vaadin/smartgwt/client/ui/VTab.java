package org.vaadin.smartgwt.client.ui;

import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import org.vaadin.smartgwt.client.ui.wrapper.TabWrapper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.tab.Tab;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTab extends Label implements Paintable, TabWrapper
{
	protected String paintableId;
	protected ApplicationConnection client;
	private Tab tab;

	public VTab()
	{
		super();
		tab = new Tab();
		setSize("1px", "1px");
	}

	/**
	 * Called whenever an update is received from the server
	 */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (client.updateComponent(this, uidl, true))
		{
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		List<Widget> widgets = PainterHelper.paintChildren(uidl, client);

		// Tabs should only have ONE child...
		if (widgets.size() != 1)
			throw new IllegalStateException("Tabs should only have one child, representing the pane");

		Widget pane = widgets.get(0);

		if (pane instanceof Canvas)
		{
			tab.setPane((Canvas) pane);
		}
		
		if (uidl.hasAttribute("*title"))
			tab.setTitle(uidl.getStringAttribute("*title"));
	}

	@Override
	public Tab getTab()
	{
		return tab;
	}
}
