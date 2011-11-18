package org.vaadin.smartgwt.client.ui.form;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.ui.VaadinManagement;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper.WidgetInfo;
import org.vaadin.smartgwt.client.ui.wrapper.FormItemWrapper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDynamicForm extends DynamicForm implements Paintable, VaadinManagement
{
	protected String paintableId;
	protected ApplicationConnection client;
	private List<WidgetInfo> widgetInfos;
	private Element dummyDiv = null;

	@Override
	public Element getElement()
	{
		if (dummyDiv == null)
		{
			dummyDiv = DOM.createDiv();
			DOMUtil.setID(dummyDiv, getID() + "_dummy");
			RootPanel.getBodyElement().appendChild(dummyDiv);
		}
		return dummyDiv;
	}

	@Override
	public void unregister()
	{
		client.unregisterPaintable(this);
		RootPanel.getBodyElement().removeChild(dummyDiv);
		dummyDiv = null;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.updateSmartGWTComponent(this, uidl);

		addFormItems(uidl, client);
	}

	private void addFormItems(UIDL uidl, ApplicationConnection client)
	{
		widgetInfos = PainterHelper.paintChildren(uidl, client);

		if (widgetInfos.size() > 0)
		{
			List<FormItem> items = new ArrayList<FormItem>();

			for (WidgetInfo widgetInfo : widgetInfos)
			{
				if (widgetInfo.getWidget() instanceof FormItemWrapper)
				{
					FormItem formItem = ((FormItemWrapper) widgetInfo.getWidget()).getFormItem();

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
