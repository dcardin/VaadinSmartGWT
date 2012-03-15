package org.vaadin.smartgwt.client.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.smartgwt.client.core.VBaseClass;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.core.BaseClass;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.util.JSON;
import com.smartgwt.client.widgets.BaseWidget;
import com.smartgwt.client.widgets.Canvas;
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

	/**
	 * Identifies the Paintable children and when associated with a widget, update them from the uidl
	 * 
	 * @param uidl
	 * @param client
	 * @return
	 */
	public static List<WidgetInfo> paintChildren(UIDL uidl, ApplicationConnection client)
	{
		List<WidgetInfo> childWidgets = createChildren(uidl, client);

		for (WidgetInfo info : childWidgets)
		{
			paintWidget(info.getWidget(), info.getUIDL(), client);
		}

		return childWidgets;
	}

	/**
	 * Create the children of this uidl so they may be referenced after
	 */
	public static List<WidgetInfo> createChildren(UIDL uidl, ApplicationConnection client)
	{
		List<WidgetInfo> childWidgets = new ArrayList<WidgetInfo>();

		int uidlCount = uidl.getChildCount();
		for (int uidlPos = 0; uidlPos < uidlCount; uidlPos++)
		{
			final UIDL childUIDL = uidl.getChildUIDL(uidlPos);
			Widget uidlWidget = childUIDL != null ? (Widget) client.getPaintable(childUIDL) : null;
			childWidgets.add(new WidgetInfo(childUIDL, uidlWidget));
		}

		return childWidgets;
	}

	public static void paintWidget(Widget widget, UIDL uidl, ApplicationConnection client)
	{
		if (widget instanceof Paintable)
		{
			((Paintable) widget).updateFromUIDL(uidl, client);
		}
	}

	/**
	 * Provides automatic processing of a Widget's property, coming from properties in uidl
	 * 
	 * @param client
	 * @param component
	 * @param uidl
	 */
	public static void updateSmartGWTComponent(ApplicationConnection client, Widget component, UIDL uidl)
	{
		if (uidl.hasAttribute("cached"))
			return;

		for (String att : uidl.getAttributeNames())
		{
			System.out.println("Updating value: " + att);

			if (!att.startsWith("*") && !att.equals("id") && !att.startsWith("_"))
			{
				if (component instanceof BaseWidget)
				{
					BaseWidget widget = (BaseWidget) component;

					if (att.equals("disabled"))
					{
						String sValue = uidl.getStringAttribute(att).substring(1);

						boolean disabled = Boolean.valueOf(sValue);
						if (disabled)
						{
							if (component instanceof Canvas)
							{
								((Canvas) component).disable();
							}
							else
							{
								((Canvas) component).enable();
							}
						}

						continue;
					}

					// Names starting with the character '#' indicate a reference to a Paintable
					if (att.startsWith("#"))
					{
						final Paintable paintable = client.getPaintable(uidl.getStringAttribute(att));

						if (paintable instanceof BaseWidget)
						{
							widget.setProperty(att.substring(1), ((BaseWidget) paintable).getOrCreateJsObj());
						}
						else if (paintable instanceof VDataClass)
						{
							final JavaScriptObject jso = ((VDataClass<DataClass>) paintable).getJSObject().getJsObj();
							widget.setProperty(att.substring(1), jso);
						}
						else if (paintable instanceof VBaseClass)
						{
							final JavaScriptObject jso = ((VBaseClass<BaseClass>) paintable).getJSObject().getOrCreateJsObj();
							widget.setProperty(att.substring(1), jso);
						}
					}
					// Names starting with '!' indicate a String[]
					else if (att.startsWith("!"))
					{
						String[] value = uidl.getStringArrayAttribute(att);
						widget.setProperty(att.substring(1), JSOHelper.convertToJavaScriptArray(value));
					}
					else
					{
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

	}

	private static void setBaseClassProperty(BaseClass data, String att, String sValue)
	{
		switch (sValue.charAt(0))
		{
			case 's':
			{
				String value = sValue.substring(1);
				if (!data.isCreated())
					JSOHelper.setAttribute(data.getConfig(), att, value);
				else
					data.setProperty(att, value);
			}
				break;

			case 'i':
			{
				Integer value = Integer.valueOf(sValue.substring(1));
				if (!data.isCreated())
					JSOHelper.setAttribute(data.getConfig(), att, value);
				else
					data.setProperty(att, value);
			}
				break;

			case 'f':
			{
				Float value = Float.valueOf(sValue.substring(1));
				if (!data.isCreated())
					JSOHelper.setAttribute(data.getConfig(), att, value);
				else
					data.setProperty(att, value);
			}
				break;

			case 'l':
			{
				Long value = Long.valueOf(sValue.substring(1));
				if (!data.isCreated())
					JSOHelper.setAttribute(data.getConfig(), att, value);
				else
					data.setProperty(att, value);
			}
				break;

			case 'd':
			{
				Double value = Double.valueOf(sValue.substring(1));
				if (!data.isCreated())
					JSOHelper.setAttribute(data.getConfig(), att, value);
				else
					data.setProperty(att, value);
			}
				break;

			case 'b':
			{
				Boolean value = Boolean.valueOf(sValue.substring(1));
				if (!data.isCreated())
					JSOHelper.setAttribute(data.getConfig(), att, value);
				else
					data.setProperty(att, value);
			}
				break;
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

			case 'j':
			{
				JavaScriptObject value = JSON.decode(sValue.substring(1));

				if (!widget.isCreated())
					JSOHelper.setAttribute(widget.getConfig(), att, value);
				else
					widget.setProperty(att, value);
			}
				break;
		}
	}

	public static void updateBaseClass(ApplicationConnection client, BaseClass dataObject, UIDL uidl)
	{
		if (uidl.hasAttribute("cached"))
			return;

		for (String att : uidl.getAttributeNames())
		{
			if (att.startsWith("#"))
			{
				Paintable p = client.getPaintable(uidl.getStringAttribute(att));
				if (p instanceof BaseWidget)
				{
					BaseWidget bw = (BaseWidget) p;
					dataObject.setAttribute(att.substring(1), bw.getOrCreateJsObj(), true);
				}
			}
			else if (att.startsWith("!"))
			{
				String[] value = uidl.getStringArrayAttribute(att);
				dataObject.setAttribute(att.substring(1), value, true);
			}
			else if (!att.startsWith("*") && !att.equals("id") && !"disabled".equalsIgnoreCase(att))
			{
				String sValue = uidl.getStringAttribute(att);
				setBaseClassProperty(dataObject, att, sValue);
			}
		}
	}

	public static void updateDataObject(ApplicationConnection client, DataClass dataObject, UIDL uidl)
	{
		if (uidl.hasAttribute("cached"))
			return;

		for (String att : uidl.getAttributeNames())
		{
			System.out.println("Updating: " + att);

			if (att.startsWith("#"))
			{
				final Paintable paintable = client.getPaintable(uidl.getStringAttribute(att));

				if (paintable instanceof BaseWidget)
				{
					dataObject.setAttribute(att.substring(1), ((BaseWidget) paintable).getOrCreateJsObj());
				}
				else if (paintable instanceof VDataClass)
				{
					final JavaScriptObject jso = ((VDataClass<DataClass>) paintable).getJSObject().getJsObj();
					dataObject.setAttribute(att.substring(1), jso);
				}
				else if (paintable instanceof VBaseClass)
				{
					final JavaScriptObject jso = ((VBaseClass<BaseClass>) paintable).getJSObject().getOrCreateJsObj();
					dataObject.setAttribute(att.substring(1), jso);
				}
			}
			else if (att.startsWith("!"))
			{
				String[] value = uidl.getStringArrayAttribute(att);
				dataObject.setAttribute(att.substring(1), value);
			}
			else if (!att.startsWith("*") && !att.equals("id") && !"disabled".equalsIgnoreCase(att))
			{
				String sValue = uidl.getStringAttribute(att);
				setDataProperty(dataObject, att, sValue);
			}
		}
	}

	public static Canvas getCanvasByRef(UIDL uidl, ApplicationConnection client, String refName)
	{
		String ref = uidl.getStringAttribute(refName);
		return (Canvas) client.getPaintable(ref);
	}
}
