package org.vaadin.smartgwt.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class PainterHelper
{
	public static List<Widget> paintChildren(UIDL uidl, ApplicationConnection client)
	{
		List<Widget> childWidgets = new ArrayList<Widget>();

		int uidlCount = uidl.getChildCount();
		for (int uidlPos = 0; uidlPos < uidlCount; uidlPos++)
		{

			final UIDL childUIDL = uidl.getChildUIDL(uidlPos);
			final Widget uidlWidget = childUIDL != null ? (Widget) client.getPaintable(childUIDL) : null;

			if (uidlWidget instanceof Paintable)
			{
				((Paintable) uidlWidget).updateFromUIDL(childUIDL, client);
			}

			childWidgets.add(uidlWidget);
		}

		return childWidgets;
	}

	public static void updateSmartGWTComponent(Widget component, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			if (!att.startsWith("*") && (!att.equalsIgnoreCase("id")))
			{
				String value = uidl.getStringAttribute(att);
				setWidgetProperty(component, att, value);
			}
		}

	}

	public static void updateSmartGWTComponentNoDimension(Widget component, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			if (!att.startsWith("*") && (!att.equals("width")) && (!att.equals("height")) && (!att.equalsIgnoreCase("id")))
			{
				String value = uidl.getStringAttribute(att);
				setWidgetProperty(component, att, value);
			}
		}

	}

	public static void updateFormItem(FormItem formItem, UIDL uidl)
	{
		for (String att : uidl.getAttributeNames())
		{
			if (!att.startsWith("*") && (!att.equalsIgnoreCase("id")))
			{
				String value = uidl.getStringAttribute(att);
				JSOHelper.setAttribute(formItem.getJsObj(), att, value);
			}
		}
	}

	public static native void setWidgetProperty(Object obj, String property, String value)/*-{
																							var widget = obj.@com.smartgwt.client.widgets.BaseWidget::getJsObj()();
																							widget.setProperty(property, value);
																							}-*/;

}
