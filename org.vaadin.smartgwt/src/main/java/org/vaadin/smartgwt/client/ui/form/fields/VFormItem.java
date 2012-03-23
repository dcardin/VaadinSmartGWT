package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.Date;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.types.FormItemType;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFormItem extends VDataClass<FormItem>
{
	private Object savedValue = null;

	public VFormItem()
	{
		super(new FormItem());

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
		Object newValue = getJSObject().getValue();

		if ((newValue == null && savedValue != null) || (newValue != null && !newValue.equals(savedValue)))
		{
			if (FormItemType.BOOLEAN.getValue().equals(getJSObject().getType()))
			{
				getClient().updateVariable(getPID(), "value", (Boolean) newValue, true);
			}
			else if (FormItemType.BOOLEAN.getValue().equals(getJSObject().getType()))
			{
				getClient().updateVariable(getPID(), "value", ((Date) newValue).toString(), true);
			}
			else
			{
				getClient().updateVariable(getPID(), "value", (String) newValue, true);
			}
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
			savedValue = uidl.getStringAttribute("value");
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
