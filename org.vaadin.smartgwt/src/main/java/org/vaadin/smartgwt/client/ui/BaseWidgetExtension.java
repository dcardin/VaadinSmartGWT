package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.BaseWidget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * 'Extension' to be used in conjunction with a client component that needs to extend a smartgwt BaseWidget.  When used, it is necessary to delegate all
 * methods to the extension.
 */
public class BaseWidgetExtension implements Paintable {
	private final BaseWidget baseWidget;
	private Element element;
	private String pid;

	public BaseWidgetExtension(BaseWidget baseWidget) {
		this.baseWidget = baseWidget;
	}
	
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (pid == null) {
			pid = uidl.getId();
		}

		PainterHelper.updateSmartGWTComponent(client, baseWidget, uidl);
	}

	public Element getElement() {
		return element == null ? element = DOM.createDiv() : element;
	}
}