package org.vaadin.smartgwt.client.ui.grid;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VListGrid extends ListGrid implements Paintable, ClientSideHandler
{
	protected String paintableId;
	protected ApplicationConnection client;
	private final ClientSideProxy rpc = new ClientSideProxy("VListGrid", this);

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	public VListGrid()
	{
		addSelectionChangedHandler(new SelectionChangedHandler()
			{
				@Override
				public void onSelectionChanged(SelectionEvent event)
				{
					JavaScriptObject selections = JSOHelper.convertToJavaScriptArray(getSelectedRecords());
					if (selections != null)
					{
						rpc.call("selectionChanged", JSON.encode(selections));
						rpc.forceSync();
					}
				}
			});
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();
		rpc.update(this, uidl, client);

		PainterHelper.paintChildren(uidl, client);

		// the dataSource property is manually managed for now. Using the automatic painter doesn't work properly
		if (uidl.hasAttribute("*dataSource"))
		{
			String ref = uidl.getStringAttribute("*dataSource");

			DataSource ds = ((Wrapper) client.getPaintable(ref)).unwrap();
			setDataSource(ds);
		}

		addListFields(uidl, client);

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
		rpc.clientInitComplete();
	}

	private void addListFields(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*fields"))
		{
			List<ListGridField> items = new ArrayList<ListGridField>();

			String[] added = uidl.getStringArrayAttribute("*fields");

			for (String c : added)
			{
				ListGridField item = ((Wrapper) client.getPaintable(c)).unwrap();
				items.add(item);
			}

			if (items.size() > 0)
			{
				ListGridField[] itemsArr = new ListGridField[0];
				itemsArr = items.toArray(itemsArr);

				setFields(itemsArr);
			}
		}
	}

	@Override
	public boolean initWidget(Object[] params)
	{
		rpc.clientInitComplete();
		return true;
	}

	@Override
	public void handleCallFromServer(String method, Object[] params)
	{}

}