package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.PaintableReferenceListener;
import org.vaadin.smartgwt.client.ui.VWindow;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VMasterContainer extends VLayout implements Paintable {
	private final PaintablePropertyUpdater paintablePropertyUpdater = new PaintablePropertyUpdater();
	private String pid;
	private ApplicationConnection client;

	public VMasterContainer() {
		setSize("100%", "100%");

		paintablePropertyUpdater.addPaintableReferenceListener("pane", new PaintableReferenceListener() {
			@Override
			public void onChange(Paintable paintable) {
				setMembers((Canvas) paintable);
			}
		});

		paintablePropertyUpdater.addPaintableListListener("window", new PaintableListListener() {
			@Override
			public void onRemove(Paintable[] source, Integer index, Paintable element) {
				((VWindow) element).destroy();
			}

			@Override
			public void onAdd(Paintable[] source, Integer index, Paintable element) {
				((VWindow) element).show();
			}
		});
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (this.pid == null) {
			this.pid = uidl.getId();
			this.client = client;
		}

		paintablePropertyUpdater.updateFromUIDL(uidl, client);
	}
}