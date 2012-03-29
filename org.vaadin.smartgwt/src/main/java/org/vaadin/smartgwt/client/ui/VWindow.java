package org.vaadin.smartgwt.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VWindow extends Window implements Paintable, ClientSideHandler
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final ClientSideProxy rpc = new ClientSideProxy("VWindow", this);
	private final Element element = DOM.createDiv();

	public VWindow()
	{
		propertyUpdater.addProperty(new PaintableProperty("members")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final List<Canvas> clientCanvases = Arrays.asList(getMembers());
					final List<Canvas> serverCanvases = new ArrayList<Canvas>(paintables.length);

					for (Paintable paintable : paintables)
					{
						serverCanvases.add((Canvas) paintable);
					}

					final List<Canvas> removedCanvases = new ArrayList<Canvas>(clientCanvases);
					removedCanvases.removeAll(serverCanvases);
					removeMembers(removedCanvases.toArray(new Canvas[0]));
					setMembers(serverCanvases.toArray(new Canvas[0]));
				}
			});
		
		propertyUpdater.addProperty(new PaintableProperty("items")
			{
				
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					for (Paintable paintable : paintables)
					{
						addItem((Canvas) paintable);
					}
				}
			});
	}

	@Override
	public Element getElement()
	{
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
		rpc.update(this, uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

	@Override
	public boolean initWidget(Object[] params)
	{
		rpc.clientInitComplete();
		return true;
	}

	@Override
	public void handleCallFromServer(String method, Object[] params)
	{

	}
}