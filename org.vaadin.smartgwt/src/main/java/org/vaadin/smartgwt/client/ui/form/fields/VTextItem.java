package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTextItem extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;

	private final TextItem ti;
	private String savedValue = null;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	public VTextItem()
	{
		super();

		ti = new TextItem();

		ti.addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		ti.addKeyPressHandler(new KeyPressHandler()
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
		String newValue = ti.getValueAsString();

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

			if (!newValue.equals(ti.getValueAsString()))
			{
				ti.setValue(newValue);
				savedValue = newValue;
			}
		}

		PainterHelper.updateDataObject(client, ti, uidl);
	}

	@Override
	public FormItem unwrap()
	{
		return ti;
	}
}
