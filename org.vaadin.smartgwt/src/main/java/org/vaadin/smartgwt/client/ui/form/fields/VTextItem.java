package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTextItem extends VDataClass<TextItem>
{
	private String savedValue = null;

	public VTextItem()
	{
		super(new TextItem());

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
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.paintChildren(uidl, getClient());
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		if (uidl.hasAttribute("value"))
		{
			String newValue = uidl.getStringAttribute("value");

			if (!newValue.equals(getJSObject().getValueAsString()))
			{
				getJSObject().setValue(newValue);
				savedValue = newValue;
			}
		}

		if (uidl.hasAttribute("*errorMessages"))
		{
			if (getJSObject().getForm() != null)
			{
				getJSObject().getForm().setFieldErrors(getJSObject().getName(), uidl.getStringArrayAttribute("*errorMessages"), true);
			}
		}
		else
		{
			if (getJSObject().getForm() != null)
			{
				getJSObject().getForm().clearFieldErrors(getJSObject().getName(), true);
			}
		}
	}
}
