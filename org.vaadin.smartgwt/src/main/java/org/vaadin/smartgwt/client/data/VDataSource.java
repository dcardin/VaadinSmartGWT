package org.vaadin.smartgwt.client.data;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.Canvas;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSource extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;
	private DataSource ds;

	public VDataSource()
	{
		super();
	}

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

		PainterHelper.paintChildren(uidl, client);

		if (uidl.hasAttribute("ID"))
		{
			ds = DataSource.get(uidl.getStringAttribute("ID"));

			if (ds == null)
			{
				ds = new DataSource();
				ds.setID(uidl.getStringAttribute("ID").substring(1));
				PainterHelper.updateBaseClass(client, ds, uidl);
				addFields(uidl, client);
			}
		}
	}

	private void addFields(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*members"))
		{
			String[] added = uidl.getStringArrayAttribute("*members");

			for (String c : added)
			{
				DataSourceField field = ((Wrapper) client.getPaintable(c)).unwrap();
				ds.addField(field);
			}
		}
	}

	@Override
	public DataSource unwrap()
	{
		return ds;
	}
}
