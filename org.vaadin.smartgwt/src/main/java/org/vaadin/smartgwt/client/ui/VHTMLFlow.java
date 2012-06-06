package org.vaadin.smartgwt.client.ui;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.HTMLFlow;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VHTMLFlow extends HTMLFlow implements Paintable {
	private final BaseWidgetExtension extension = new BaseWidgetExtension(this);

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		extension.updateFromUIDL(uidl, client);
		if (uidl.hasAttribute("*contentsResource")) {
			setContentsURL(client.translateVaadinUri(uidl.getStringAttribute("*contentsResource")));
		} else {
			setContentsURL(null);
		}
	}

	@Override
	public Element getElement() {
		return extension.getElement();
	}
}
