package org.vaadin.smartgwt.client.ui.form.fields;

import com.smartgwt.client.widgets.form.fields.TextItem;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTextItem extends VAbstractFormItem<TextItem, String>
{
	public VTextItem()
	{
		super(new TextItem());
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
