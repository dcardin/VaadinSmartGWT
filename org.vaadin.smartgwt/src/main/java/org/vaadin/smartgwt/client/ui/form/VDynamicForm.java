package org.vaadin.smartgwt.client.ui.form;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDynamicForm extends DynamicForm implements Paintable
{
	protected String paintableId;
	protected ApplicationConnection client;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.paintChildren(uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);

		addFormItems(uidl, client);
	}

	private void addFormItems(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*fields"))
		{
			List<FormItem> items = new ArrayList<FormItem>();

			String[] added = uidl.getStringArrayAttribute("*fields");

			for (String c : added)
			{
				FormItem item = ((Wrapper) client.getPaintable(c)).unwrap();
				items.add(item);
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
