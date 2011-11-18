package org.vaadin.smartgwt.client.ui.layout;

import java.util.List;

import org.vaadin.smartgwt.client.ui.VaadinManagement;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VHLayout extends HLayout implements Paintable, VaadinManagement
{
	protected String paintableId;
	protected ApplicationConnection client;
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
		widgetInfos = PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("*children-painted"))
		{
			removeMembers(getMembers());

			for (WidgetInfo widgetInfo : widgetInfos)
			{
				addMember(widgetInfo.getWidget());
			}
		}
		else
		{
			if (uidl.hasAttribute("*replaced"))
			{
				String[] replaced = uidl.getStringArrayAttribute("*replaced");
				for (int i = 0; i < replaced.length; i += 2)
				{
					Canvas removed = (Canvas) client.getPaintable(replaced[i]);
					Canvas added = (Canvas) client.getPaintable(replaced[i + 1]);
					int pos = getMemberNumber(removed);
					removeMember(removed);

					if (removed instanceof VaadinManagement)
					{
						((VaadinManagement) removed).unregister();
					}

					addMember(added, pos);
				}
			}
			if (uidl.hasAttribute("*removed"))
			{
				String[] removed = uidl.getStringArrayAttribute("*removed");
				for (String c : removed)
				{
					Canvas removedCanvas = (Canvas) client.getPaintable(c);
					removeMember(removedCanvas);
					if (removedCanvas instanceof VaadinManagement)
					{
						((VaadinManagement) removedCanvas).unregister();
					}

				}
			}
			if (uidl.hasAttribute("*added"))
			{
				String[] added = uidl.getStringArrayAttribute("*added");
				for (String c : added)
				{
					addMember((Canvas) client.getPaintable(c));
				}
			}
		}
	}
}