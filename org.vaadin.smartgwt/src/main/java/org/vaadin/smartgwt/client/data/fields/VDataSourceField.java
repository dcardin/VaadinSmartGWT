package org.vaadin.smartgwt.client.data.fields;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.Canvas;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSourceField extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;
	private DataSourceField dsf;
	
	public VDataSourceField()
	{
		super();
		dsf = new DataSourceField();
	}
	
	@Override
	public DataSourceField unwrap()
	{
		return dsf;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.paintChildren(uidl, client);
		PainterHelper.updateDataObject(client, dsf, uidl);
	}
}
