package org.vaadin.smartgwt.client.extra;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VRefresher extends Widget implements Paintable {
	private final Poller poller;
	private Element element;
	private String pid;
	private ApplicationConnection client;

	public VRefresher() {
		this.poller = new Poller();
	}

	@Override
	public Element getElement() {
		return element == null ? element = DOM.createDiv() : element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (this.pid == null) {
			this.pid = uidl.getId();
			this.client = client;
		}

		if (!uidl.hasAttribute("cached")) {
			poller.cancel();
			if (uidl.hasAttribute("interval")) {
				poller.scheduleRepeating(uidl.getIntAttribute("interval"));
			}
		}
	}

	private class Poller extends Timer {
		@Override
		public void run() {
			client.updateVariable(pid, "r", true, true);
		}
	}
}
