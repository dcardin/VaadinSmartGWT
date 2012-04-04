package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VAbstractFormItem<T extends FormItem, V> extends VDataClass<T>
{
	private V value = null;

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
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		if (uidl.hasAttribute("value"))
		{
			value = getUIDLFormItemValue(uidl, "value");
		}

		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	protected abstract V getUIDLFormItemValue(UIDL uidl, String attributeName);

	protected abstract V getFormItemValue();

	private void postChange()
	{
		if (!equal(value, getFormItemValue()))
		{
			getClient().updateVariable(getPID(), "value", getFormItemValue() == null ? null : getFormItemValue().toString(), true);
		}
	}

	private static boolean equal(Object a, Object b)
	{
		return a == b || (a != null && a.equals(b));
	}
}
