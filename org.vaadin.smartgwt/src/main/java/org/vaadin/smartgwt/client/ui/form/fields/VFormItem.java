package org.vaadin.smartgwt.client.ui.form.fields;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFormItem extends VAbstractFormItem<FormItem, String>
{
	public VFormItem()
	{
		super(new FormItem());
	}

	@Override
	protected String getUIDLFormItemValue(UIDL uidl, String attributeName)
	{
		return uidl.getStringAttribute(attributeName);
	}

	@Override
	protected String getFormItemValue()
	{
		return getJSObject().getValue() == null ? null : getJSObject().getValue().toString();
	}

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
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
