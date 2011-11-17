package org.vaadin.smartgwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.BaseWidget;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class PainterHelper
{
	public static class WidgetInfo
	{
		private final Widget widget;
		private final UIDL uidl;

		public WidgetInfo(UIDL uidl, Widget widget)
		{
			this.uidl = uidl;
			this.widget = widget;
		}

		public Widget getWidget()
		{
			return widget;
		}

		public UIDL getUIDL()
		{
			return uidl;
		}
	}

	public static List<WidgetInfo> paintChildren(UIDL uidl, ApplicationConnection client)
	{
		List<WidgetInfo> childWidgets = new ArrayList<WidgetInfo>();

		int uidlCount = uidl.getChildCount();
		for (int uidlPos = 0; uidlPos < uidlCount; uidlPos++)
		{
			final UIDL childUIDL = uidl.getChildUIDL(uidlPos);

			Widget uidlWidget = childUIDL != null ? (Widget) client.getPaintable(childUIDL) : null;

			if (uidlWidget instanceof Paintable)
			{
				((Paintable) uidlWidget).updateFromUIDL(childUIDL, client);
			}

			childWidgets.add(new WidgetInfo(childUIDL, uidlWidget));
		}

		return childWidgets;
	}

	public static void updateSmartGWTComponent(Widget component, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			if (!att.startsWith("*"))// && !att.equals("id"))
			{
				if (component instanceof BaseWidget)
				{
					BaseWidget widget = (BaseWidget) component;

					String sValue = uidl.getStringAttribute(att);

					if (sValue.equals("null"))
					{
						widget.setProperty(att, (String) null);
					}
					else if (sValue.length() == 0)
					{
						widget.setProperty(att, "");
					}
					else
					{
						setWidgetProperty(widget, att, sValue);
					}
				}
			}
		}

	}

	private static void setDataProperty(DataClass data, String att, String sValue)
	{
		Object value = null;

		switch (sValue.charAt(0))
		{
			case 's':
				value = sValue.substring(1);
				break;

			case 'i':
				value = Integer.valueOf(sValue.substring(1));
				break;
			case 'f':
				value = Float.valueOf(sValue.substring(1));
				break;

			case 'l':
				value = Long.valueOf(sValue.substring(1));
				break;

			case 'd':
				value = Double.valueOf(sValue.substring(1));
				break;

			case 'b':
				value = Boolean.valueOf(sValue.substring(1));
				break;
		}

		data.setAttribute(att, value);
	}

	private static void setWidgetProperty(BaseWidget widget, String att, String sValue)
	{
		switch (sValue.charAt(0))
		{
			case 's':
			{
				String value = sValue.substring(1);
				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;

			case 'i':
			{
				Integer value = Integer.valueOf(sValue.substring(1));
				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;

			case 'f':
			{
				Float value = Float.valueOf(sValue.substring(1));
				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;

			case 'l':
			{
				Long value = Long.valueOf(sValue.substring(1));
				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;

			case 'd':
			{
				Double value = Double.valueOf(sValue.substring(1));
				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;

			case 'b':
			{
				Boolean value = Boolean.valueOf(sValue.substring(1));
				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;
		}
	}

	public static void updateFormItem(FormItem formItem, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			System.out.println("name:" + att + ", value:" + uidl.getStringAttribute(att));
		}

		for (String att : uidl.getAttributeNames())
		{
			if (!att.startsWith("*") && !att.equals("id"))
			{
				String sValue = uidl.getStringAttribute(att);
				setDataProperty(formItem, att, sValue);
			}
		}
	}

}
