package org.vaadin.smartgwt.client.ui.layout;

import java.util.List;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;

import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMasterContainer extends VLayout implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;
	private List<WidgetInfo> widgetInfos;

	public VMasterContainer()
	{
		setSize("100%", "100%");
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		if (uidl.hasAttribute("*children-painted"))
		{
			widgetInfos = PainterHelper.paintChildren(uidl, client);

			removeMembers(getMembers());

			for (WidgetInfo widgetInfo : widgetInfos)
			{
				addMember(widgetInfo.getWidget());
			}
		}
	}
}