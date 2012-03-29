package org.vaadin.smartgwt.client.ui.form;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.ui.form.fields.VAbstractFormItem;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDynamicForm extends DynamicForm implements Paintable
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final Element element = DOM.createDiv();

	public VDynamicForm()
	{
		propertyUpdater.addProperty(new PaintableProperty("fields")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					final List<FormItem> formItems = new ArrayList<FormItem>();

					for (Paintable paintable : paintables)
					{
						formItems.add(((VAbstractFormItem<? extends FormItem, ?>) paintable).getJSObject());
					}

					setFields(formItems.toArray(new FormItem[0]));
				}
			});
	}

	@Override
	public Element getElement()
	{
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}
}
