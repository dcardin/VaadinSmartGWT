package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.core.PaintableArrayListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.events.BlurEvent;
import com.smartgwt.client.widgets.form.fields.events.BlurHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyPressEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyPressHandler;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public abstract class VAbstractFormItem<T extends FormItem, V> extends VDataClass<T>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
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

		propertyUpdater.addPaintableArrayListener("icons", new PaintableArrayListener()
			{
				@Override
				public void onChange(Paintable[] oldPaintables, Paintable[] newPaintables)
				{
					final FormItemIcon[] icons = new FormItemIcon[newPaintables.length];

					for (int i = 0; i < newPaintables.length; i++)
					{
						icons[i] = ((VFormItemIcon) newPaintables[i]).getJSObject();
					}

					getJSObject().setIcons(icons);
				}
			});
	}

	@Override
	protected void preAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
		super.preAttributeUpdateFromUIDL(uidl, client);
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
