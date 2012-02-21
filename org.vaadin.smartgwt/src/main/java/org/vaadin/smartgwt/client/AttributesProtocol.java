package org.vaadin.smartgwt.client;

import static org.vaadin.smartgwt.shared.AttributesProtocolConstant.*;

import java.util.Iterator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.BaseWidget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class AttributesProtocol
{
	public static void update(ApplicationConnection client, UIDL uidl, BaseWidget widget)
	{
		for (Iterator<Object> iterator = Iterators.filter(uidl.getChildIterator(), isAttributeUIDL()); iterator.hasNext();)
		{
			final UIDL attributeUIDL = (UIDL) iterator.next();
			final String name = attributeUIDL.getStringAttribute(ATTRIBUTE_NAME);
			final String type = attributeUIDL.getStringAttribute(ATTRIBUTE_TYPE);

			if (TYPE_STRING.equals(type))
			{
				final String value = attributeUIDL.getStringAttribute(ATTRIBUTE_VALUE);

				if (!widget.isCreated())
				{
					JSOHelper.setAttribute(widget.getConfig(), name, value);
				}
				else
				{
					widget.setProperty(name, value);
				}
			}
			else if (TYPE_INT.equals(type))
			{
				final int value = attributeUIDL.getIntAttribute(ATTRIBUTE_VALUE);

				if (!widget.isCreated())
				{
					JSOHelper.setAttribute(widget.getConfig(), name, value);
				}
				else
				{
					widget.setProperty(name, value);
				}
			}
			else if (TYPE_LONG.equals(type))
			{
				final long value = attributeUIDL.getLongAttribute(ATTRIBUTE_VALUE);

				if (!widget.isCreated())
				{
					JSOHelper.setAttribute(widget.getConfig(), name, value);
				}
				else
				{
					widget.setProperty(name, value);
				}
			}
			else if (TYPE_FLOAT.equals(type))
			{
				final float value = attributeUIDL.getFloatAttribute(ATTRIBUTE_VALUE);

				if (!widget.isCreated())
				{
					JSOHelper.setAttribute(widget.getConfig(), name, value);
				}
				else
				{
					widget.setProperty(name, value);
				}
			}
			else if (TYPE_DOUBLE.equals(type))
			{
				final double value = attributeUIDL.getDoubleAttribute(ATTRIBUTE_VALUE);

				if (!widget.isCreated())
				{
					JSOHelper.setAttribute(widget.getConfig(), name, value);
				}
				else
				{
					widget.setProperty(name, value);
				}
			}
			else if (TYPE_BOOLEAN.equals(type))
			{
				final boolean value = attributeUIDL.getBooleanAttribute(ATTRIBUTE_VALUE);

				if (!widget.isCreated())
				{
					JSOHelper.setAttribute(widget.getConfig(), name, value);
				}
				else
				{
					widget.setProperty(name, value);
				}
			}
		}
	}

	private static Predicate<Object> isAttributeUIDL()
	{
		return new Predicate<Object>()
			{
				@Override
				public boolean apply(Object child)
				{
					return child instanceof UIDL && TAG_ATTRIBUTE.equals(((UIDL) child).getTag());
				}
			};
	}
}
