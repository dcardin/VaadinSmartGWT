package org.vaadin.smartgwt;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

public class BaseComponent extends AbstractComponent
{
	private String name = "";
	private boolean isOpaque;
	private String height = null;
	private String width = null;

	public void setHeight(String height)
	{
		this.height = height;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		if (name != null)
			target.addAttribute("name", name);

		if (width != null)
			target.addAttribute("s-width", width);

		if (height != null)
			target.addAttribute("s-height", height);
	}

}
