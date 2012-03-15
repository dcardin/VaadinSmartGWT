package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.core.VBaseClass;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSelectItem extends VDataClass<SelectItem>
{
	private String savedValue = null;

	public VSelectItem()
	{
		super(new SelectItem());

		getJSObject().addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		getJSObject().addKeyPressHandler(new KeyPressHandler()
			{

				@Override
				public void onKeyPress(KeyPressEvent event)
				{
					if (event.getKeyName().equalsIgnoreCase("enter"))
					{
						postChange();
					}
				}
			});
	}

	private void postChange()
	{
		String newValue = getJSObject().getValueAsString();

		if ((newValue == null && savedValue != null) || (newValue != null && !newValue.equals(savedValue)))
		{
			getClient().updateVariable(getPID(), "value", newValue, true);
		}
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		if (uidl.hasAttribute("value"))
		{
			String newValue = uidl.getStringAttribute("value");

			if (!newValue.equals(getJSObject().getValueAsString()))
			{
				savedValue = newValue;
			}
		}

		// the dataSource property is manually managed for now. Using the automatic painter doesn't work properly
		if (uidl.hasAttribute("*optionDataSource"))
		{
			String ref = uidl.getStringAttribute("*optionDataSource");

			DataSource ds = VBaseClass.getBaseClass(getClient(), ref);
			getJSObject().setOptionDataSource(ds);
		}

		addListFields(uidl, getClient());
	}
	
	private void addListFields(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*pickListFields"))
		{
			List<ListGridField> items = new ArrayList<ListGridField>();

			String[] added = uidl.getStringArrayAttribute("*pickListFields");

			for (String c : added)
			{
				ListGridField item = VDataClass.getDataClass(client, c);
				items.add(item);
			}

			if (items.size() > 0)
			{
				ListGridField[] itemsArr = new ListGridField[0];
				itemsArr = items.toArray(itemsArr);

				getJSObject().setPickListFields(itemsArr);
			}
		}
	}
}
