package org.vaadin.smartgwt.server;

import org.vaadin.smartgwt.server.types.Alignment;


class BlueBox extends Label
{

	public BlueBox(String contents)
	{
		super("");
		setAlign(Alignment.CENTER);
		setBorder("1px solid #808080");
		setBackgroundColor("#C3D9FF");
		setContents(contents);
	}

	public BlueBox(Integer width, Integer height, String contents)
	{
		this(contents);
		if (width != null)
			setWidth(width);
		if (height != null)
			setHeight(height);
	}

	public BlueBox(Integer width, String height, String contents)
	{
		this(contents);
		if (width != null)
			setWidth(width);
		if (height != null)
			setHeight(height);
	}

	public BlueBox(String width, String height, String contents)
	{
		this(contents);
		if (width != null)
			setWidth(width);
		if (height != null)
			setHeight(height);
	}
}