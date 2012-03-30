package org.vaadin.smartgwt.client.data;

import org.vaadin.smartgwt.client.core.PaintableListListener;
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

		propertyUpdater.addPaintableListListener("fields", new PaintableListListener()
			{
				@Override
				public void onAdd(Paintable[] source, Integer index, Paintable element)
				{
					getJSObject().setFields(toDataSourceFieldArray(source));
				}

				@Override
				public void onRemove(Paintable[] source, Integer index, Paintable element)
				{
					getJSObject().setFields(toDataSourceFieldArray(source));
				}

				private DataSourceField[] toDataSourceFieldArray(Paintable[] source)
				{
					final DataSourceField[] fields = new DataSourceField[source.length];

					for (int i = 0; i < source.length; i++)
					{
						fields[i] = ((VDataClass<DataSourceField>) source[i]).getJSObject();
					}

					return fields;
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
