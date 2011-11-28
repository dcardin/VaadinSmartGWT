package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionHeader;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSectionStackSection extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;
	private SectionStackSection section = new SectionStackSection();

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateDataObject(client, section, uidl);
		PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("*members"))
		{
			String[] members = uidl.getStringArrayAttribute("*members");
			for (String c : members)
			{
				section.addItem((Canvas) client.getPaintable(c));
			}
		}

		if (uidl.hasAttribute("icon"))
		{
			section.setAttribute("icon", uidl.getStringAttribute("icon").substring(1));
		}

	}

	@Override
	public SectionStackSection unwrap()
	{
		return section;
	}
}