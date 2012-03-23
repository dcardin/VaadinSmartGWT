package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
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
	private static Element dummyDiv;
	private final PaintablePropertyUpdater paintablePropertyUpdater = new PaintablePropertyUpdater();

	static
	{
		if (dummyDiv == null)
		{
			dummyDiv = DOM.createDiv();
			DOMUtil.setID(dummyDiv, "dummy_placeholder");
			RootPanel.getBodyElement().appendChild(dummyDiv);
		}
	}

	public static Element getDummy()
	{
		return dummyDiv;
	}

	public VMasterContainer()
	{
		setSize("100%", "100%");

		paintablePropertyUpdater.addProperty(new PaintableProperty("pane")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					setMembers((Canvas) paintables[0]);
				}
			});

		paintablePropertyUpdater.addProperty(new PaintableProperty("window")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					((VWindow) paintables[0]).show();
				}
			});
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		paintablePropertyUpdater.updateFromUIDL(uidl, client);
	}
}