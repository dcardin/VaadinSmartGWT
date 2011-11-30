package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.smartgwt.client.widgets.form.validator.CustomValidator;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFormItem extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;

	private final FormItem fi;
	private Object savedValue = null;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	public VFormItem()
	{
		super();

//		CustomValidator validator = new CustomValidator()
//			{
//				@Override
//				protected boolean condition(Object value)
//				{
//					if (fi.getAttribute("errorMessage") != null && fi.getAttribute("errorMessage").length() > 0)
//					{
//						setErrorMessage(fi.getAttribute("errorMessage"));
//						return false;
//					}
//					return true;
//				}
//			};

		fi = new FormItem();

//		fi.setValidators(validator);

		fi.addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		fi.addKeyPressHandler(new KeyPressHandler()
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
		Object newValue = fi.getValue();

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
			savedValue = uidl.getStringAttribute("value");
		}

		PainterHelper.updateDataObject(client, fi, uidl);

		if (uidl.hasAttribute("*errorMessages"))
		{
			if (fi.getForm() != null)
			{
				fi.getForm().setFieldErrors(fi.getName(), uidl.getStringArrayAttribute("*errorMessages"), true);
			}
		}
		else
		{
			if (fi.getForm() != null)
			{
				fi.getForm().clearFieldErrors(fi.getName(), true);
			}
		}
	}

	@Override
	public FormItem unwrap()
	{
		return fi;
	}

}
