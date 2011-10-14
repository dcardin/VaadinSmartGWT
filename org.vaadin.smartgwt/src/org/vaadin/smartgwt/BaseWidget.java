package org.vaadin.smartgwt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.AbstractComponentContainer;

public abstract class BaseWidget extends AbstractComponent implements Serializable
{
	protected Map<String, Object> attributes = new HashMap<String, Object>();

	boolean isCreated()
	{
		return false;
	}

	protected void setAttribute(String attribute, Object value, boolean allowPostCreate)
	{
		attributes.put(attribute, value);

		/*
		 * if (!isCreated()) { JSOHelper.setAttribute(config, attribute, value); } else if (allowPostCreate) { setProperty(attribute, value); } else {
		 * error(attribute, value); }
		 */
	}

	protected void setAttribute(String attribute, Object value)
	{
		setAttribute(attribute, value, false);
	}

	protected String getAttributeAsString(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return value.toString();
	}

	protected String getAttribute(String attribute)
	{
		return getAttributeAsString(attribute);
	}

	protected Integer getAttributeAsInt(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return Integer.valueOf(value.toString());
	}

	protected Boolean getAttributeAsBoolean(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return Boolean.valueOf(value.toString());
	}

	protected String[] getAttributeAsStringArray(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return (String[]) value;
	}
	
	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		for (Map.Entry<String, Object> entry : attributes.entrySet())
		{
			target.addAttribute(entry.getKey(), entry.getValue().toString());
		}
	}

}
