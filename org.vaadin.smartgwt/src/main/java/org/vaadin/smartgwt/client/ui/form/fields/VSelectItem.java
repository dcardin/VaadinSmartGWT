package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VJSObject;
import org.vaadin.smartgwt.client.ui.grid.VListGridField;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSelectItem extends VAbstractFormItem<SelectItem, String>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VSelectItem()
	{
		super(new SelectItem());

		propertyUpdater.addProperty(new PaintableProperty("pickListFields")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final List<ListGridField> pickListFields = new ArrayList<ListGridField>();

					for (Paintable paintable : paintables)
					{
						pickListFields.add(((VListGridField) paintable).getJSObject());
					}

					getJSObject().setPickListFields(pickListFields.toArray(new ListGridField[0]));
				}
			});
	}

	@Override
	protected String getUIDLFormItemValue(UIDL uidl, String attributeName)
	{
		return uidl.getStringAttribute(attributeName);
	}

	@Override
	protected String getFormItemValue()
	{
		return getJSObject().getValueAsString();
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		propertyUpdater.updateFromUIDL(uidl, getClient());

		// the dataSource property is manually managed for now. Using the automatic painter doesn't work properly
		if (uidl.hasAttribute("optionDataSource"))
		{
			final Paintable paintable = uidl.getPaintableAttribute("optionDataSource", getClient());
			getJSObject().setOptionDataSource((DataSource) ((VJSObject<?>) paintable).getJSObject());
		}
	}
}
