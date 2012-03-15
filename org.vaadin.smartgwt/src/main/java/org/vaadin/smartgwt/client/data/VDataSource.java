package org.vaadin.smartgwt.client.data;

import org.vaadin.smartgwt.client.core.VBaseClass;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSource extends VBaseClass<DataSource>
{
	private final boolean isRegistered = false;

	public VDataSource()
	{
		super(new DataSource());
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		if (uidl.hasAttribute("*ID") && !isRegistered)
		{
			getJSObject().setID(uidl.getStringAttribute("*ID").substring(1));
		}

		if (uidl.hasAttribute("*members"))
		{
			String[] added = uidl.getStringArrayAttribute("*members");

			for (String c : added)
			{
				getJSObject().addField(VDataClass.<DataSourceField> getDataClass(getClient(), c));
			}
		}
	}
}
