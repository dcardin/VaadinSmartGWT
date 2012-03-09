package org.vaadin.smartgwt.server;

import org.vaadin.smartgwt.server.layout.MasterContainer;

public class WindowFactory
{
	private final MasterContainer masterContainer;

	public WindowFactory(MasterContainer masterContainer)
	{
		this.masterContainer = masterContainer;
	}

	public Window newWindow()
	{
		return new Window(masterContainer);
	}
}
