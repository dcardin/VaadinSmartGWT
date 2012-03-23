package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSectionStackSection extends VDataClass<SectionStackSection>
{
	public VSectionStackSection()
	{
		super(new SectionStackSection());
	}

	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.paintChildren(uidl, getClient());
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		if (uidl.hasAttribute("*members"))
		{
			String[] members = uidl.getStringArrayAttribute("*members");

			for (String c : members)
			{
				getJSObject().addItem((Canvas) getClient().getPaintable(c));
			}
		}

		if (uidl.hasAttribute("icon"))
		{
			getJSObject().setAttribute("icon", uidl.getStringAttribute("icon").substring(1));
		}
	}
}