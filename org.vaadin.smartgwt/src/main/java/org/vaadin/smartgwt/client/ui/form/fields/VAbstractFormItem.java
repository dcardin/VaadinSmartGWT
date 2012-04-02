package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VAbstractFormItem<T extends FormItem, V> extends VDataClass<T>
{
	private V value = null;
	private ApplicationConnection client;
	private String pid;

	protected VAbstractFormItem(T dataClass)
	{
		super(dataClass);

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

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.pid = uidl.getId();
		this.client = client;

		if (uidl.hasAttribute("value"))
		{
			value = getUIDLFormItemValue(uidl, "value");
		}
	}

	protected abstract V getUIDLFormItemValue(UIDL uidl, String attributeName);

	protected abstract V getFormItemValue();

	private void postChange()
	{
		if (!equal(value, getFormItemValue()))
		{
			client.updateVariable(pid, "value", getFormItemValue() == null ? null : getFormItemValue().toString(), true);
		}
	}

	private static boolean equal(Object a, Object b)
	{
		return a == b || (a != null && a.equals(b));
	}
}
