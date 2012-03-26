package org.vaadin.smartgwt.client.data;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VBaseClass;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSource extends VBaseClass<DataSource>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final boolean isRegistered = false;

	public VDataSource()
	{
		super(new DataSource());

		propertyUpdater.addProperty(new PaintableProperty("fields")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final DataSourceField[] fields = new DataSourceField[paintables.length];

					for (int i = 0; i < paintables.length; i++)
					{
						fields[i] = ((VDataClass<DataSourceField>) paintables[i]).getJSObject();
					}

					getJSObject().setFields(fields);
				}
			});
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		propertyUpdater.updateFromUIDL(uidl, getClient());

		if (uidl.hasAttribute("*ID") && !isRegistered)
		{
			getJSObject().setID(uidl.getStringAttribute("*ID").substring(1));
		}
	}
}
