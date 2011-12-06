package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VVSplitLayout extends VLayout implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;
	private ResizedHandler handler;
	private boolean resizeWithParent;

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

		Canvas top = PainterHelper.getCanvasByRef(uidl, client, "#top");
		Canvas bottom = PainterHelper.getCanvasByRef(uidl, client, "#bottom");

		boolean showResizeBar = Boolean.parseBoolean(uidl.getStringAttribute("*showResizeBar").substring(1));
		resizeWithParent = getAttributeAsBoolean("resizeWithParent");

		setupLayout(top, bottom, showResizeBar, uidl.getStringArrayAttribute("*proportions"));

		if (handler == null)
		{
			handler = new ResizedHandler()
				{
					@Override
					public void onResized(ResizedEvent event)
					{
						if (resizeWithParent)
						{
							updateChildrenSizes();
						}
						reflow();
					}

				};
		}
		addResizedHandler(handler);
	};

	@Override
	protected void onDraw()
	{
		super.onDraw();
		updateChildrenSizes();
	}

	private void updateChildrenSizes()
	{
		if (resizeWithParent)
		{
			int max = 0;
			for (Canvas child : getMembers())
			{
				if (child.getVisibleWidth() > max)
					max = child.getVisibleWidth();
			}
			for (Canvas child : getMembers())
			{
				child.setWidth(max);
			}
			setWidth(max);
		}
	}

	private void setupLayout(final Canvas top, final Canvas bottom, boolean showResizeBar, String[] proportions)
	{
		setHeight100();

		removeMembers(getMembers());

		top.setHeight(proportions[0]);
		bottom.setHeight(proportions[1]);

		top.setShowResizeBar(showResizeBar);

		addMember(top);
		addMember(bottom);
	}
}