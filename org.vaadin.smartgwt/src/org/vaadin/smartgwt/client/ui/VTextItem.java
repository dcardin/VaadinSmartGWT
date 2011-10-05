package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.ui.wrapper.FormItemWrapper;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTextItem extends Label implements Paintable, FormItemWrapper
{
	protected String paintableId;
	protected ApplicationConnection client;

	private TextItem ti;
	private String savedValue = null;

	public VTextItem()
	{
		super();
		setSize("1px", "1px");

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
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (client.updateComponent(this, uidl, true))
		{
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateFormItem(ti, uidl);

		if (uidl.hasAttribute("value"))
		{
			String newValue = uidl.getStringAttribute("value");

			if (!newValue.equals(ti.getValueAsString()))
			{
				ti.setValue(newValue);
				savedValue = newValue;
			}
		}

		if (uidl.hasAttribute("title"))
			ti.setTitle(uidl.getStringAttribute("title"));

	}

	@Override
	public FormItem getFormItem()
	{
		return ti;
	}

}
