package org.vaadin.smartgwt.client.ui.grid;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VListGrid extends ListGrid implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(client, this, uidl);
		PainterHelper.paintChildren(uidl, client);
		
		addListFields(uidl, client);
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

}