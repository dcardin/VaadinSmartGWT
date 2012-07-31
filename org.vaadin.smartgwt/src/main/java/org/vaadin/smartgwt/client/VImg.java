package org.vaadin.smartgwt.client;

import org.vaadin.smartgwt.client.ui.BaseWidgetExtension;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Img;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VImg extends Img implements Paintable {
	private final BaseWidgetExtension extension = new BaseWidgetExtension(this);

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		extension.updateFromUIDL(uidl, client);
	}

	@Override
	public Element getElement() {
		return extension.getElement();
	}
}
