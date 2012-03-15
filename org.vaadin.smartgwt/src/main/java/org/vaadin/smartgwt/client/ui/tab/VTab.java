package org.vaadin.smartgwt.client.ui.tab;

import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTab extends VDataClass<Tab>
{
	public VTab()
	{
		super(new Tab());
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
