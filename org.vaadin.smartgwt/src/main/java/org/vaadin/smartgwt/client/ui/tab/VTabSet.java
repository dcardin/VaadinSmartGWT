package org.vaadin.smartgwt.client.ui.tab;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.rpc.shared.Method;
import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTabSet extends TabSet implements Paintable, ClientSideHandler
{
	private final ClientSideProxy rpc = new ClientSideProxy("VTabSet", this);
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VTabSet()
	{
		super();
		rpc.register("selectTab", new Method()
			{
				@Override
				public void invoke(final String methodName, final Object[] data)
				{
					selectTab((Integer) data[0]);
				}
			});

		propertyUpdater.addProperty(new PaintableProperty("tabs")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final List<Tab> clientTabs = Arrays.asList(getTabs());
					final List<Tab> serverTabs = new ArrayList<Tab>(paintables.length);

					for (int i = 0; i < paintables.length; i++)
					{
						serverTabs.add(((VDataClass<Tab>) paintables[i]).getJSObject());
					}

					final List<Tab> removedTabs = new ArrayList<Tab>(clientTabs);
					removedTabs.removeAll(serverTabs);

					for (Tab removedTab : removedTabs)
					{
						removeTab(removedTab);
					}
					
					setTabs(serverTabs.toArray(new Tab[0]));
				}
			});
	}

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
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
		System.out.println("method call: " + method);
	}
}
