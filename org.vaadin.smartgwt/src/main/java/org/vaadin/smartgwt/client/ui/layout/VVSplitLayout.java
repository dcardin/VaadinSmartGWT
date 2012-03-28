package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VVSplitLayout extends VLayout implements Paintable
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private boolean resizeWithParent;

	public VVSplitLayout()
	{
		setHeight100();
		setMembers(new NullMember(), new NullMember());

		addResizedHandler(new ResizedHandler()
			{
				@Override
				public void onResized(ResizedEvent event)
				{
					updateChildrenSizes();
					reflow();
				}
			});

		propertyUpdater.addProperty(new PaintableProperty("top")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final Canvas top = (Canvas) paintables[0];
					removeMember(getMembers()[0]);
					addMember(top, 0);
				}
			});

		propertyUpdater.addProperty(new PaintableProperty("bottom")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final Canvas bottom = (Canvas) paintables[0];
					removeMember(getMembers()[1]);
					addMember(bottom);
				}
			});
	}

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("cached"))
		{
			return;
		}

		propertyUpdater.updateFromUIDL(uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
		resizeWithParent = uidl.getBooleanAttribute("*resizeWithParent");
	}

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

	private static class NullMember extends Label
	{
		public NullMember()
		{
			super("");
			setVisible(false);
		}
	}
}