package org.vaadin.smartgwt;

import org.vaadin.smartgwt.server.IButton;
import org.vaadin.smartgwt.server.layout.BorderLayout;
import org.vaadin.smartgwt.server.layout.VLayout;

import com.vaadin.Application;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class BorderLayoutApplication extends Application
{
	private BorderLayout bl;
	private final String[] texts = { "NORTH", "SOUTH", "CENTER", "EAST", "WEST" };

	@Override
	public void init()
	{
		Window mainWindow = new Window("Borderlayout Application");
		VLayout vlo = new VLayout();

		bl = new BorderLayout();
		bl.setSizeFull();
		bl.setNorthComponent(new IButton("north"));
		bl.setSouthComponent(new IButton("south"));
		bl.setCenterComponent(new IButton("center"));
		bl.setEastComponent(new IButton("east"));
		bl.setWestComponent(new IButton("west"));

		// mainWindow.setContent(bl);
		setMainWindow(mainWindow);
	}

}
