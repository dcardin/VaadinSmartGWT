package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.PaintableReferenceListener;
import org.vaadin.smartgwt.client.ui.VWindow;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMasterContainer extends VLayout implements Paintable
{
	private final PaintablePropertyUpdater paintablePropertyUpdater = new PaintablePropertyUpdater();

	public VMasterContainer()
	{
		setSize("100%", "100%");

		paintablePropertyUpdater.addPaintableReferenceListener("pane", new PaintableReferenceListener()
			{
				@Override
				public void onChange(Paintable paintable)
				{
					setMembers((Canvas) paintable);
				}
			});

		paintablePropertyUpdater.addPaintableReferenceListener("window", new PaintableReferenceListener()
			{
				@Override
				public void onChange(Paintable paintable)
				{
					((VWindow) paintable).show();
				}
			});
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		paintablePropertyUpdater.updateFromUIDL(uidl, client);
	}
}