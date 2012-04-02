package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.Date;

import com.smartgwt.client.widgets.form.fields.DateItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDateItem extends VAbstractFormItem<DateItem, Date>
{
	public VDateItem()
	{
		super(new DateItem());
	}

	@Override
	protected Date getUIDLFormItemValue(UIDL uidl, String attributeName)
	{
		return new Date(uidl.getLongAttribute(attributeName));
	}

	@Override
	protected Date getFormItemValue()
	{
		return getJSObject().getValueAsDate();
	}

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{

	}
}
