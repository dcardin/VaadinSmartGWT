package org.vaadin.smartgwt.client.data.fields;

import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.data.DataSourceField;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSourceField extends VDataClass<DataSourceField>
{
	public VDataSourceField()
	{
		super(new DataSourceField());
	}
	
	@Override
	protected void updateFromUIDL(UIDL uidl)
	{

	}
}
