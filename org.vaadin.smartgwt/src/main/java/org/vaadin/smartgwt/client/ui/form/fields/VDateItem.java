package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.Date;

import org.vaadin.smartgwt.client.ui.VaadinManagement;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.wrapper.FormItemWrapper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.util.DOMUtil;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDateItem extends Label implements Paintable, FormItemWrapper, VaadinManagement
{
	protected String paintableId;
	protected ApplicationConnection client;

	private final DateItem di;
	private final Date savedValue = null;
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

	public VDateItem()
	{
		super();
		setSize("1px", "1px");

		di = new DateItem();

		di.addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		di.addKeyPressHandler(new KeyPressHandler()
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
		Date newValue = di.getValueAsDate();

		if ((newValue == null && savedValue != null) || (newValue != null && !newValue.equals(savedValue)))
		{
			client.updateVariable(paintableId, "value", newValue.toString(), true);
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
			// DateFormat df = new SimpleDateFormat();
			// Date newValue = null;
			// try
			// {
			// newValue = df.parse(uidl.getStringAttribute("value"));
			// }
			// catch (ParseException e)
			// {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			//
			// if (newValue != null && newValue.equals(di.getValueAsDate()))
			// {
			// //ti.setValue(newValue);
			// savedValue = newValue;
			// }
		}

		PainterHelper.updateFormItem(di, uidl);
	}

	@Override
	public FormItem getFormItem()
	{
		return di;
	}

}
