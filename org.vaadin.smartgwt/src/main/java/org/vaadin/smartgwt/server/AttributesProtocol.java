package org.vaadin.smartgwt.server;

import static org.vaadin.smartgwt.shared.AttributesProtocolConstant.*;

import java.util.Map;
import java.util.Map.Entry;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public class AttributesProtocol
{
	public static void paint(PaintTarget target, Map<String, Object> attributes)
	{
		try
		{
			for (Entry<String, Object> attribute : attributes.entrySet())
			{
				paint(target, attribute.getKey(), attribute.getValue());
			}
		}
		catch (PaintException e)
		{
			throw new RuntimeException(e);
		}
	}

	public static void paint(PaintTarget target, String name, Object value) throws PaintException
	{
		if (value instanceof String)
		{
			paint(target, name, (String) value);
		}
		else if (value instanceof Integer)
		{
			paint(target, name, (Integer) value);
		}
		else if (value instanceof Long)
		{
			paint(target, name, (Long) value);
		}
		else if (value instanceof Float)
		{
			paint(target, name, (Float) value);
		}
		else if (value instanceof Double)
		{
			paint(target, name, (Double) value);
		}
		else if (value instanceof Boolean)
		{
			paint(target, name, (Boolean) value);
		}
		// else if (value instanceof Map)
		// {
		// target.addAttribute(ATTRIBUTE_TYPE, TYPE_MAP);
		// target.addAttribute(ATTRIBUTE_VALUE, (Map<?, ?>) value);
		// }
		// else if (value instanceof String[])
		// {
		// target.addAttribute(ATTRIBUTE_TYPE, TYPE_STRING_ARRAY);
		// target.addAttribute(ATTRIBUTE_VALUE, (String[]) value);
		// }
		// else if (value instanceof Integer[])
		// {
		// target.addAttribute(ATTRIBUTE_TYPE, TYPE_INT_ARRAY);
		// target.addAttribute(ATTRIBUTE_VALUE, (Integer[]) value);
		// }
		// else if (value instanceof Paintable)
		// {
		// target.addAttribute(ATTRIBUTE_TYPE, TYPE_PID);
		// target.addAttribute(ATTRIBUTE_VALUE, (Paintable) value);
		// }
		// else if (value instanceof Paintable[])
		// {
		// target.addAttribute(ATTRIBUTE_TYPE, TYPE_PID_ARRAY);
		//
		// for (Paintable paintable : (Paintable[]) value)
		// {
		// target.startTag(TAG_PAINTABLE);
		// target.addAttribute(ATTRIBUTE_TYPE, TYPE_PID);
		// target.addAttribute(ATTRIBUTE_VALUE, paintable);
		// target.endTag(TAG_PAINTABLE);
		// }
		// }
	}

	public static void paint(PaintTarget target, String name, String value) throws PaintException
	{
		target.startTag(TAG_ATTRIBUTE);
		target.addAttribute(ATTRIBUTE_NAME, name);
		target.addAttribute(ATTRIBUTE_TYPE, TYPE_STRING);
		target.addAttribute(ATTRIBUTE_VALUE, value);
		target.endTag(TAG_ATTRIBUTE);
	}

	public static void paint(PaintTarget target, String name, Integer value) throws PaintException
	{
		target.startTag(TAG_ATTRIBUTE);
		target.addAttribute(ATTRIBUTE_NAME, name);
		target.addAttribute(ATTRIBUTE_TYPE, TYPE_INT);
		target.addAttribute(ATTRIBUTE_VALUE, value);
		target.endTag(TAG_ATTRIBUTE);
	}

	public static void paint(PaintTarget target, String name, Long value) throws PaintException
	{
		target.startTag(TAG_ATTRIBUTE);
		target.addAttribute(ATTRIBUTE_NAME, name);
		target.addAttribute(ATTRIBUTE_TYPE, TYPE_LONG);
		target.addAttribute(ATTRIBUTE_VALUE, value);
		target.endTag(TAG_ATTRIBUTE);
	}

	public static void paint(PaintTarget target, String name, Float value) throws PaintException
	{
		target.startTag(TAG_ATTRIBUTE);
		target.addAttribute(ATTRIBUTE_NAME, name);
		target.addAttribute(ATTRIBUTE_TYPE, TYPE_FLOAT);
		target.addAttribute(ATTRIBUTE_VALUE, value);
		target.endTag(TAG_ATTRIBUTE);
	}

	public static void paint(PaintTarget target, String name, Double value) throws PaintException
	{
		target.startTag(TAG_ATTRIBUTE);
		target.addAttribute(ATTRIBUTE_NAME, name);
		target.addAttribute(ATTRIBUTE_TYPE, TYPE_DOUBLE);
		target.addAttribute(ATTRIBUTE_VALUE, value);
		target.endTag(TAG_ATTRIBUTE);
	}

	public static void paint(PaintTarget target, String name, Boolean value) throws PaintException
	{
		target.startTag(TAG_ATTRIBUTE);
		target.addAttribute(ATTRIBUTE_NAME, name);
		target.addAttribute(ATTRIBUTE_TYPE, TYPE_BOOLEAN);
		target.addAttribute(ATTRIBUTE_VALUE, value);
		target.endTag(TAG_ATTRIBUTE);
	}
}
