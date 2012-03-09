package org.vaadin.smartgwt.client.ui.layout;

import java.util.List;

import org.vaadin.smartgwt.client.data.VDataSource;
import org.vaadin.smartgwt.client.ui.VWindow;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMasterContainer extends VLayout implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;

	public static Element dummyDiv;

	public static Element getDummy()
	{
		return dummyDiv;
	}

	static
	{
		if (dummyDiv == null)
		{
			dummyDiv = DOM.createDiv();
			DOMUtil.setID(dummyDiv, "dummy_placeholder");
			RootPanel.getBodyElement().appendChild(dummyDiv);
		}
	}

	public VMasterContainer()
	{
		setSize("100%", "100%");
	}
	
	
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		List<WidgetInfo> widgetInfos = PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("*pane"))
		{
			Canvas canvas = PainterHelper.getCanvasByRef(uidl, client, "*pane");
			System.out.println("Canvas: " + canvas.getHeightAsString() + ", " + canvas.getWidthAsString());
			removeMembers(getMembers());
			addMember(canvas);
		}

		if (uidl.hasAttribute("*window"))
		{
			VWindow window = (VWindow) PainterHelper.getCanvasByRef(uidl, client, "*window");
			window.show();
		}
	}

	public void updateFromUIDLold(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		List<WidgetInfo> widgetInfos = PainterHelper.createChildren(uidl, client);

		
		// Register all datasources first
		for (WidgetInfo wi : widgetInfos)
		{
			Widget widget = wi.getWidget();
			
			if (widget instanceof VDataSource)
			{
				PainterHelper.paintWidget(widget, uidl, client);
			}
		}
		
		if (uidl.hasAttribute("pane"))
		{
			Canvas canvas = PainterHelper.getCanvasByRef(uidl, client, uidl.getStringAttribute("pane"));
			PainterHelper.paintWidget(canvas, uidl, client);
			removeMembers(getMembers());
			addMember(canvas);
		}

		if (uidl.hasAttribute("window"))
		{
			VWindow window = (VWindow) PainterHelper.getCanvasByRef(uidl, client, uidl.getStringAttribute("window"));
			PainterHelper.paintWidget(window, uidl, client);
			window.show();
		}
		
	}
	
}