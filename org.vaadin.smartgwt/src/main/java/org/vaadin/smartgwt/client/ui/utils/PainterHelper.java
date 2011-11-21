package org.vaadin.smartgwt.client.ui.utils;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.core.BaseClass;
import com.smartgwt.client.core.DataClass;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.BaseWidget;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.tab.Tab;
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

	public static void updateSmartGWTComponent(ApplicationConnection client, Widget component, UIDL uidl)
	{
		if (uidl.hasAttribute("cached"))
			return;

		for (String att : uidl.getAttributeNames())
		{
			if (!att.startsWith("*"))// && !att.equals("id"))
			{
				if (component instanceof BaseWidget)
				{
					BaseWidget widget = (BaseWidget) component;

					if (att.startsWith("["))
					{
						String[] refs = uidl.getStringArrayAttribute(att);
						List<Canvas> paintables = new ArrayList<Canvas>();

						for (String c : refs)
						{
							Paintable p = client.getPaintable(c);
							if (p instanceof Wrapper)
							{
								Canvas canvas = ((Wrapper) p).unwrap();
								paintables.add(canvas);
							}
						}

						Canvas[] toSet = new Canvas[0];
						toSet = paintables.toArray(toSet);

						// component.setAttribute(att.substring(1), toSet);
					}
					else if (att.startsWith("#"))
					{
						Paintable paintable = client.getPaintable(uidl.getStringAttribute(att));
						if (paintable instanceof Canvas)
						{
							Object obj = null;

							if (paintable instanceof Wrapper)
							{
								obj = ((Wrapper) paintable).unwrap();
							}

							if (obj instanceof Canvas)
							{
								widget.setProperty(att.substring(1), ((Canvas) obj).getOrCreateJsObj());
							}
							else if (obj instanceof BaseClass)
							{
								JavaScriptObject jso = ((BaseClass) obj).getOrCreateJsObj();
								widget.setProperty(att.substring(1), jso);
							}
						}
					}
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
		}
	}

	public static void updateBaseClass(ApplicationConnection client, BaseClass dataObject, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			if (att.startsWith("["))
			{
				String[] refs = uidl.getStringArrayAttribute(att);
				List<Canvas> paintables = new ArrayList<Canvas>();

				for (String c : refs)
				{
					Paintable p = client.getPaintable(c);
					if (p instanceof Wrapper)
					{
						Canvas canvas = ((Wrapper) p).unwrap();
						paintables.add(canvas);
					}
				}

				Canvas[] toSet = new Canvas[0];
				toSet = paintables.toArray(toSet);
				// dataObject.setAttribute(att.substring(1), toSet);
			}
			else if (att.startsWith("#"))
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
			else if (!att.startsWith("*") && !att.equals("id"))
			{
				String sValue = uidl.getStringAttribute(att);
				setBaseClassProperty(dataObject, att, sValue);
			}
		}
	}

	public static void updateDataObject(ApplicationConnection client, DataClass dataObject, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			if (att.startsWith("["))
			{
				String[] refs = uidl.getStringArrayAttribute(att);
				List<Canvas> paintables = new ArrayList<Canvas>();

				for (String c : refs)
				{
					Paintable p = client.getPaintable(c);
					if (p instanceof Wrapper)
					{
						Canvas canvas = ((Wrapper) p).unwrap();
						paintables.add(canvas);
					}
				}

				Canvas[] toSet = new Canvas[0];
				toSet = paintables.toArray(toSet);
				// dataObject.setAttribute(att.substring(1), toSet);
			}
			else if (att.startsWith("#"))
			{
				Paintable paintable = client.getPaintable(uidl.getStringAttribute(att));
				if (paintable instanceof Canvas)
				{
					Object obj = paintable;

					if (paintable instanceof Wrapper)
					{
						obj = ((Wrapper) paintable).unwrap();
					}

					if (obj instanceof Canvas)
					{
						dataObject.setAttribute(att.substring(1), ((Canvas) obj).getOrCreateJsObj());
					}
					else if (obj instanceof BaseClass)
					{
						JavaScriptObject jso = ((BaseClass) obj).getOrCreateJsObj();
						dataObject.setAttribute(att.substring(1), jso);
					}
				}
			}
			else if (att.startsWith("!"))
			{
				String[] value = uidl.getStringArrayAttribute(att);
				dataObject.setAttribute(att.substring(1), value);
			}
			else if (!att.startsWith("*") && !att.equals("id"))
			{
				String sValue = uidl.getStringAttribute(att);
				setDataProperty(dataObject, att, sValue);
			}
		}
	}
}
