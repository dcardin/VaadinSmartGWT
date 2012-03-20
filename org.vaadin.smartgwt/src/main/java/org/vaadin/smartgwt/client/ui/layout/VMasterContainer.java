package org.vaadin.smartgwt.client.ui.layout;


import java.util.ArrayList;
import java.util.List;

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
	private PaintablePropertyUpdater paintablePropertyUpdater;

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
		this.paintablePropertyUpdater = newPaintablePropertyUpdater();
		setSize("100%", "100%");
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		paintablePropertyUpdater.updateFromUIDL(uidl, client);
	}

	private PaintablePropertyUpdater newPaintablePropertyUpdater()
	{
		final List<PaintableProperty> paintableProperties = new ArrayList<PaintableProperty>();
		paintableProperties.add(PaintableProperty.forName("dataSources"));
		paintableProperties.add(new PaintableProperty("pane")
			{
				@Override
				public void postUpdate(Paintable paintable)
				{
					setMembers((Canvas) paintable);
				}
			});
		paintableProperties.add(new PaintableProperty("window")
			{
				@Override
				public void postUpdate(Paintable paintable)
				{
					((VWindow) paintable).show();
				}
			});

		return new PaintablePropertyUpdater(paintableProperties);
	}
}