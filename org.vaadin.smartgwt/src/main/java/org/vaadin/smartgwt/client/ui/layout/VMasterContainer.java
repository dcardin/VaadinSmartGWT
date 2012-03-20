package org.vaadin.smartgwt.client.ui.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
		updateChildrenFromUIDL(uidl, client, "dataSources");
		updateChildrenFromUIDL(uidl, client, "pane", new PaintableFunction()
			{
				@Override
				public void execute(Paintable paintable)
				{
					setMembers((Canvas) paintable);
				}
			});
		updateChildrenFromUIDL(uidl, client, "window", new PaintableFunction()
			{
				@Override
				public void execute(Paintable paintable)
				{
					((VWindow) paintable).show();
				}
			});
	}

	private void updateChildrenFromUIDL(UIDL uidl, ApplicationConnection client, String tagName)
	{
		_updateChildrenFromUIDL(uidl, client, tagName);
	}

	private void updateChildrenFromUIDL(UIDL uidl, ApplicationConnection client, String tagName, PaintableFunction function)
	{
		final List<Paintable> paintables = _updateChildrenFromUIDL(uidl, client, tagName);

		if (!paintables.isEmpty())
		{
			function.execute(paintables.get(0));
		}
	}

	private List<Paintable> _updateChildrenFromUIDL(UIDL uidl, ApplicationConnection client, String tagName)
	{
		final List<Paintable> paintables = new ArrayList<Paintable>();
		final UIDL childUIDL = uidl.getChildByTagName(tagName);

		if (childUIDL != null)
		{
			for (Iterator<Object> iterator = childUIDL.getChildIterator(); iterator.hasNext();)
			{
				final UIDL paintableUIDL = (UIDL) iterator.next();
				final Paintable paintable = client.getPaintable(paintableUIDL);
				paintable.updateFromUIDL(paintableUIDL, client);
				paintables.add(paintable);
			}
		}

		return paintables;
	}

	private static interface PaintableFunction
	{
		void execute(Paintable paintable);
	}
}