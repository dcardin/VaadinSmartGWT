package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VHSplitLayout extends HLayout implements Paintable
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

		Canvas left = PainterHelper.getCanvasByRef(uidl, client, "#left");
		Canvas right = PainterHelper.getCanvasByRef(uidl, client, "#right");

		boolean showResizeBar = Boolean.parseBoolean(uidl.getStringAttribute("*showResizeBar").substring(1));
		resizeWithParent = getAttributeAsBoolean("resizeWithParent");

		setupLayout(left, right, showResizeBar, uidl.getStringArrayAttribute("*proportions"));

		if (handler == null)
		{
			handler = new ResizedHandler()
				{
					@Override
					public void onResized(ResizedEvent event)
					{
						redraw();
//						updateChildrenSizes();
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
				if (child.getVisibleHeight() > max)
					max = child.getVisibleHeight();
			}
			for (Canvas child : getMembers())
			{
				child.setHeight(max);
			}
			setHeight(max);
		}
	}

	private void setupLayout(final Canvas left, final Canvas right, boolean showResizeBar, String[] proportions)
	{
		setWidth100();

		removeMembers(getMembers());

		left.setWidth(proportions[0]);
		right.setWidth(proportions[1]);

		left.setShowResizeBar(showResizeBar);

		addMember(left);
		addMember(right);
	}
}