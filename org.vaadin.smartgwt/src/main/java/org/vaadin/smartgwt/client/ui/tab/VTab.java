package org.vaadin.smartgwt.client.ui.tab;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTab extends VDataClass<Tab>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VTab()
	{
		super(new Tab());
		
		propertyUpdater.addProperty(new PaintableProperty("pane")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					getJSObject().setPane(paintables.length > 0 ? (Canvas) paintables[0] : null);
				}
			});
	}

	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		propertyUpdater.updateFromUIDL(uidl, getClient());
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
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
