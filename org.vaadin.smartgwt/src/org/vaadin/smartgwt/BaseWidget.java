package org.vaadin.smartgwt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

public class BaseWidget extends AbstractComponent implements Serializable
{
	private Map<String, Object> attributes = new HashMap<String, Object>();
	protected PartialPaintChecker paintChecker = new PartialPaintChecker(this);

	boolean isCreated()
	{
		return false;
	}

	@Override
	public void requestRepaint()
	{
		paintChecker.checkBeforeRequestRepaint();
		super.requestRepaint();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		if (paintChecker.isFullRepaint())
		{
			for (Map.Entry<String, Object> entry : attributes.entrySet())
			{
				target.addAttribute("*" + entry.getKey(), entry.getValue().toString());
			}
		}
		else
		{
			for (String attribute : paintChecker.getFlagged())
			{
				target.addAttribute("*" + attribute, getAttributeAsString(attribute));
			}
		}

		paintChecker.paintContentPerformed();
	}

	protected void setAttribute(String attribute, Object value, boolean allowPostCreate)
	{
		attributes.put(attribute, value);

		/*
		 * if (!isCreated()) { JSOHelper.setAttribute(config, attribute, value); } else if (allowPostCreate) { setProperty(attribute, value); } else {
		 * error(attribute, value); }
		 */
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
}
