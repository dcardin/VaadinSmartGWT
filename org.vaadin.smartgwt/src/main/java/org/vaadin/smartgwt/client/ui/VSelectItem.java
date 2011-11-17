package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.ui.wrapper.FormItemWrapper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSelectItem extends Label implements Paintable, FormItemWrapper, VaadinManagement
{
	protected String paintableId;
	protected ApplicationConnection client;

	private final SelectItem si;
	private String savedValue = null;
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

	public VSelectItem()
	{
		super();
		setSize("1px", "1px");

		si = new SelectItem();

		si.addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		si.addKeyPressHandler(new KeyPressHandler()
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
		String newValue = si.getValueAsString();

		if ((newValue == null && savedValue != null) || (newValue != null && !newValue.equals(savedValue)))
		{
			client.updateVariable(paintableId, "value", newValue, true);
		}
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		if (uidl.hasAttribute("value"))
		{
			String newValue = uidl.getStringAttribute("value");

			if (!newValue.equals(si.getValueAsString()))
			{
				// ti.setValue(newValue);
				savedValue = newValue;
			}
		}

		PainterHelper.updateFormItem(si, uidl);
	}

	@Override
	public FormItem getFormItem()
	{
		return si;
	}

}
