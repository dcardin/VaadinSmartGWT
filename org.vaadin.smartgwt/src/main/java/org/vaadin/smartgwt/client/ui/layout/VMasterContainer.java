package org.vaadin.smartgwt.client.ui.layout;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.data.VDataSource;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.data.DataSource;
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

//		PainterHelper.updateSmartGWTComponent(client, this, uidl);
		List<WidgetInfo> widgetInfos = createChildren(uidl);

		// Register all datasources first
		for (WidgetInfo wi : widgetInfos)
		{
			if (wi.getWidget() instanceof VDataSource)
			{
				((Paintable) wi.getWidget()).updateFromUIDL(wi.getUIDL(), client);
				
				DataSource ds = ((Wrapper) wi.getWidget()).unwrap();
				System.out.println("Found datasource " + ds.getID());
				// register the datasource
//				PainterHelper.updateBaseClass(client, ds, uidl); 
			}
		}

		for (WidgetInfo wi : widgetInfos)
		{
			if (wi.getWidget() instanceof VDataSource == false)
			{
				((Paintable) wi.getWidget()).updateFromUIDL(wi.getUIDL(), client);
				addMember(wi.getWidget());
			}
		}
		
		// Now, just the pane
//		String ref = uidl.getStringAttribute("pane");
//		addMember((Canvas) client.getPaintable(ref));
	}
	

	public List<WidgetInfo> createChildren(UIDL uidl)
	{
		List<WidgetInfo> childWidgets = new ArrayList<WidgetInfo>();

		int uidlCount = uidl.getChildCount();
		for (int uidlPos = 0; uidlPos < uidlCount; uidlPos++)
		{
			final UIDL childUIDL = uidl.getChildUIDL(uidlPos);
			Widget uidlWidget = childUIDL != null ? (Widget) client.getPaintable(childUIDL) : null;
			childWidgets.add(new WidgetInfo(childUIDL, uidlWidget));
		}

		return childWidgets;
	}

}