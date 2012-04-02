package org.vaadin.smartgwt.client.ui.tab;

import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.PaintableReferenceListener;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTab extends VDataClass<Tab>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VTab()
	{
		super(new Tab());
		
		propertyUpdater.addPaintableReferenceListener("pane", new PaintableReferenceListener()
			{
				@Override
				public void onChange(Paintable paintable)
				{
					getJSObject().setPane((Canvas) paintable);
				}
			});
	}

	@Override
	protected void preAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
	}

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("title"))
		{
			if (getJSObject().getIcon() != null)
			{
				getJSObject().setTitle(
						Canvas.imgHTML(getJSObject().getIcon()) + " <span class=\"tabTitle\" valign=\"center\" align=\"center\">"
								+ uidl.getStringAttribute("title").substring(1));
			}
		}
	}
}
