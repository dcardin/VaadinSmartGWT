package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSelectItem extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;

	private final SelectItem si;
	private String savedValue = null;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	public VSelectItem()
	{
		super();

		si = new SelectItem();

		si.addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		si.addKeyPressHandler(new KeyPressHandler()
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
		String newValue = si.getValueAsString();

		if ((newValue == null && savedValue != null) || (newValue != null && !newValue.equals(savedValue)))
		{
			client.updateVariable(paintableId, "value", newValue, true);
		}
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		if (uidl.hasAttribute("value"))
		{
			String newValue = uidl.getStringAttribute("value");

			if (!newValue.equals(si.getValueAsString()))
			{
				// ti.setValue(newValue);
				savedValue = newValue;
			}
		}

		PainterHelper.paintChildren(uidl, client);

		// the dataSource property is manually managed for now. Using the automatic painter doesn't work properly
		if (uidl.hasAttribute("*optionDataSource"))
		{
			String ref = uidl.getStringAttribute("*optionDataSource");

			DataSource ds = ((Wrapper) client.getPaintable(ref)).unwrap();
			si.setOptionDataSource(ds);
		}

		addListFields(uidl, client);
		PainterHelper.updateDataObject(client, si, uidl);
	}
	
	private void addListFields(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*pickListFields"))
		{
			List<ListGridField> items = new ArrayList<ListGridField>();

			String[] added = uidl.getStringArrayAttribute("*pickListFields");

			for (String c : added)
			{
				ListGridField item = ((Wrapper) client.getPaintable(c)).unwrap();
				items.add(item);
			}

			if (items.size() > 0)
			{
				ListGridField[] itemsArr = new ListGridField[0];
				itemsArr = items.toArray(itemsArr);

				si.setPickListFields(itemsArr);
			}
		}
	}

	@Override
	public FormItem unwrap()
	{
		return si;
	}
	


}
