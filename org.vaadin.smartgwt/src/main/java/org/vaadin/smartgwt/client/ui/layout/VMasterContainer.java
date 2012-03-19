package org.vaadin.smartgwt.client.ui.layout;

import java.util.Iterator;

import org.vaadin.smartgwt.client.ui.VWindow;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMasterContainer extends VLayout implements Paintable
{
	public static Element dummyDiv;

	public static Element getDummy()
	{
		return dummyDiv;
	}

	static
	{
		if (dummyDiv == null)
		{
			dummyDiv = DOM.createDiv();
			DOMUtil.setID(dummyDiv, "dummy_placeholder");
			RootPanel.getBodyElement().appendChild(dummyDiv);
		}
	}

	public VMasterContainer()
	{
		setSize("100%", "100%");
	}
	
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		final UIDL paneUIDL = uidl.getChildByTagName("pane");

		if (paneUIDL != null)
		{
			final UIDL componentUIDL = paneUIDL.getChildUIDL(0);
			final Paintable component = client.getPaintable(componentUIDL);
			component.updateFromUIDL(componentUIDL, client);
			setMembers((Canvas) component);
		}

		for (Iterator<Object> iterator = uidl.getChildIterator(); iterator.hasNext();)
		{
			final Object next = iterator.next();

			if (next instanceof UIDL)
			{
				final UIDL childUIDL = (UIDL) next;

				if (childUIDL.getTag() != "pane")
				{
					client.getPaintable(childUIDL).updateFromUIDL(childUIDL, client);
				}
			}
		}

		if (uidl.hasAttribute("*window"))
		{
			((VWindow) uidl.getPaintableAttribute("*window", client)).show();
		}
	}
}