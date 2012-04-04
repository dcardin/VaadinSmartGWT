package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.PaintableReferenceListener;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

abstract class VSplitLayoutHelper
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final Layout source;
	private boolean resizeWithParent;

	VSplitLayoutHelper(Layout source)
	{
		this.source = source;
		source.setMembers(new NullMember(), new NullMember());

		source.addResizedHandler(new ResizedHandler()
			{
				@Override
				public void onResized(ResizedEvent event)
				{
					updateChildrenSizes();
					VSplitLayoutHelper.this.source.reflow();
				}
			});

		propertyUpdater.addPaintableReferenceListener("member1", new PaintableReferenceListener()
			{
				@Override
				public void onChange(Paintable paintable)
				{
					final Canvas member1 = (Canvas) paintable;
					VSplitLayoutHelper.this.source.removeMember(VSplitLayoutHelper.this.source.getMembers()[0]);
					VSplitLayoutHelper.this.source.addMember(member1, 0);
				}
			});

		propertyUpdater.addPaintableReferenceListener("member2", new PaintableReferenceListener()
			{
				@Override
				public void onChange(Paintable paintable)
				{
					final Canvas member2 = (Canvas) paintable;
					VSplitLayoutHelper.this.source.removeMember(VSplitLayoutHelper.this.source.getMembers()[1]);
					VSplitLayoutHelper.this.source.addMember(member2);
				}
			});
	}
	
	void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("cached"))
		{
			return;
		}

		propertyUpdater.updateFromUIDL(uidl, client);
		PainterHelper.updateSmartGWTComponent(client, source, uidl);
		resizeWithParent = uidl.getBooleanAttribute("*resizeWithParent");
	}

	void updateChildrenSizes()
	{
		if (resizeWithParent)
		{
			int max = 0;

			for (Canvas child : source.getMembers())
			{
				if (getVisibleDimension(child) > max)
				{
					max = getVisibleDimension(child);
				}
			}

			for (Canvas child : source.getMembers())
			{
				setDimension(child, max);
			}

			setDimension(source, max);
		}
	}
	
	protected abstract int getVisibleDimension(Canvas canvas);

	protected abstract void setDimension(Canvas canvas, int dimension);

	private static class NullMember extends Label
	{
		public NullMember()
		{
			super("");
			setVisible(false);
		}
	}
}