package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.core.ServerSideEventRegistration;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VRadioGroupItem extends ToolStripButton implements Paintable {
	private final Element element = DOM.createDiv();
	private String pid;
	private ApplicationConnection client;
	private ServerSideEventRegistration clickHandlerEventRegistration;

	public VRadioGroupItem() {
		super();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (pid == null) {
			this.pid = uidl.getId();
			this.client = client;

			clickHandlerEventRegistration = new ServerSideEventRegistration("*hasClickHandlers") {
				@Override
				protected HandlerRegistration registerHandler() {
					return addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							VRadioGroupItem.this.client.updateVariable(pid, "clickEvent", true, true);
						}
					});
				}
			};
		}

		clickHandlerEventRegistration.updateFromUIDL(uidl);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

	@Override
	public Element getElement() {
		return element;
	}
}
