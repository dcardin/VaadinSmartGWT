package org.vaadin.smartgwt.client.data;

import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VBaseClass;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDataSource extends VBaseClass<DataSource>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VDataSource()
	{
		super(new DataSource());

		propertyUpdater.addPaintableListListener("fields", new PaintableListListener()
			{
				@Override
				public void onAdd(Paintable[] source, Integer index, Paintable element)
				{
					getJSObject().addField(((VDataClass<DataSourceField>) element).getJSObject());
				}

				@Override
				public void onRemove(Paintable[] source, Integer index, Paintable element)
				{

				}
			});
	}

	@Override
	protected void preAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
	}

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*ID") && getJSObject().getID() == null)
		{
			getJSObject().setID(uidl.getStringAttribute("*ID").substring(1));
		}
	}
}
