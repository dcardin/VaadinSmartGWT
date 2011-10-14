package org.vaadin.smartgwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import org.vaadin.smartgwt.client.ui.wrapper.FormItemWrapper;
import com.smartgwt.client.types.Positioning;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDynamicForm extends DynamicForm implements Paintable
{
	protected String paintableId;
	ApplicationConnection client;

	public VDynamicForm()
	{
		super();
	}

	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (client.updateComponent(this, uidl, true))
		{
			return;
		}

		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		addFormItems(uidl, client);

		if (getPosition() != Positioning.ABSOLUTE)
			setPosition(Positioning.ABSOLUTE);
	}

	private void addFormItems(UIDL uidl, ApplicationConnection client)
	{
		List<Widget> widgets = PainterHelper.paintChildren(uidl, client);

		if (widgets.size() > 0)
		{
			List<FormItem> items = new ArrayList<FormItem>();

			for (Widget widget : widgets)
			{
				if (widget instanceof FormItemWrapper)
				{
					FormItem formItem = ((FormItemWrapper) widget).getFormItem();

					if (formItem != null)
						items.add(formItem);
				}
			}

			if (items.size() > 0)
			{
				FormItem[] itemsArr = new FormItem[0];
				itemsArr = items.toArray(itemsArr);

				setFields(itemsArr);
			}
		}
	}
}
