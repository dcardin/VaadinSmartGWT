package org.vaadin.smartgwt;

import com.vaadin.Application;
import com.vaadin.ui.*;

public class SmartGWTApplication extends Application {
	@Override
	public void init() {
		Window mainWindow = new Window("SmartGWT vaadin integration");
		Label label = new Label("Hello Vaadin user");
		mainWindow.addComponent(label);
		setMainWindow(mainWindow);
	}

}
