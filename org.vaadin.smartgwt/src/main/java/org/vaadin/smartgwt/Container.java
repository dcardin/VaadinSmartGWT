package org.vaadin.smartgwt;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponentContainer;
import com.vaadin.ui.Component;

public class Container extends AbstractComponentContainer
{
	protected List<Component> components = new LinkedList<Component>();
	private String height = null;
	private String width = null;

	@Override
	public void setSizeFull()
	{
		setWidth("100%");
		setHeight("100%");
	}

	public void setHeight(String height)
	{
		this.height = height;
	}

	public void setWidth(String width)
	{
		this.width = width;
	}

	@Override
	public void addComponent(Component c)
	{
		components.add(c);
		super.addComponent(c);
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent)
	{

	}

	@Override
	public Iterator<Component> getComponentIterator()
	{
		return components.iterator();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		for (Component c : components)
		{
			c.paint(target);
		}

		if (width != null)
			target.addAttribute("s-width", width);

		if (height != null)
			target.addAttribute("s-height", height);

	}

}
