package org.vaadin.smartgwt.client.data.fields;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.data.DataSourceField;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSourceField extends VDataClass<DataSourceField>
{
	public VDataSourceField()
	{
		super(new DataSourceField());
	}
	
	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.paintChildren(uidl, getClient());
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{

	}
}
