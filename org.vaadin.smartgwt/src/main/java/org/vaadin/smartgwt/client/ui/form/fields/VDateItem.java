package org.vaadin.smartgwt.client.ui.form.fields;

import java.util.Date;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.UIDL;

public class VDateItem extends VDataClass<DateItem>
{
	private final Date savedValue = null;

	public VDateItem()
	{
		super(new DateItem());

		getJSObject().addBlurHandler(new BlurHandler()
			{
				@Override
				public void onBlur(BlurEvent event)
				{
					postChange();
				}
			});

		getJSObject().addKeyPressHandler(new KeyPressHandler()
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
		Date newValue = getJSObject().getValueAsDate();

		if ((newValue == null && savedValue != null) || (newValue != null && !newValue.equals(savedValue)))
		{
			getClient().updateVariable(getPID(), "value", newValue.toString(), true);
		}
	}

	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.paintChildren(uidl, getClient());
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{

	}
}
