package org.vaadin.smartgwt;

import com.vaadin.Application;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class BorderLayoutApplication extends Application {
    private BorderLayout bl;
    private String[] texts = { "NORTH", "SOUTH", "CENTER", "EAST", "WEST" };

    @Override
    public void init() {
        Window mainWindow = new Window("Borderlayout Application");
        VLayout vlo = new VLayout();

        bl = new BorderLayout();
        bl.setSizeFull();
        bl.addComponent(new IButton("north"), BorderLayout.Constraint.NORTH);
        bl.addComponent(new IButton("south"), BorderLayout.Constraint.SOUTH);
        bl.addComponent(new IButton("center"), BorderLayout.Constraint.CENTER);
        bl.addComponent(new IButton("east"), BorderLayout.Constraint.EAST);
        bl.addComponent(new IButton("west"), BorderLayout.Constraint.WEST);

        mainWindow.setContent(bl);
        setMainWindow(mainWindow);
    }

}
