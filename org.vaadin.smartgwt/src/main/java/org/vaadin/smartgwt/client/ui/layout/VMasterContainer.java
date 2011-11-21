package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
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

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
		PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("*members"))
		{
			removeMembers(getMembers());
			String[] members = uidl.getStringArrayAttribute("*members");

			for (String member : members)
			{
				addMember((Canvas) client.getPaintable(member));
			}
		}

		// if (uidl.hasAttribute("*children-painted"))
		// {
		// widgetInfos = PainterHelper.paintChildren(uidl, client);
		//
		// removeMembers(getMembers());
		//
		// for (WidgetInfo widgetInfo : widgetInfos)
		// {
		// addMember(widgetInfo.getWidget());
		// }
		// }
	}
}