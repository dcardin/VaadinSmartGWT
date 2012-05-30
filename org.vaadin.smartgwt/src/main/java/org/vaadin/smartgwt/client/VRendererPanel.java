package org.vaadin.smartgwt.client;

import java.util.Date;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VRendererPanel extends VLayout implements Paintable {
	private Img main;
	private boolean onMain = true;

	private final ClientSideProxy rpc = new ClientSideProxy("VRendererPanel", new ClientSideHandlerImpl());
	private String pid;

	public VRendererPanel() {
		main = new Img();
		main.setHeight("99%");
		main.setWidth("99%");
		
		addMember(main);

		addResizedHandler(new ResizedHandler() {
			@Override
			public void onResized(ResizedEvent event) {
				refresh();
			}
		});
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (pid == null) {
			pid = uidl.getId();
		}

		rpc.update(this, uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}

	public void refresh() {
		String width = getWidthAsString();
		String height = getHeightAsString();

		DateTimeFormat format = DateTimeFormat.getFormat("MMddyyyyHHmmssS");
		String date = format.format(new Date());

		main.setBackgroundImage(GWT.getHostPageBaseURL() + "imageFetcher?type=main&width=" + width + "&height=" + height + "&time=" + date);
	}

	private class ClientSideHandlerImpl implements ClientSideHandler {
		@Override
		public boolean initWidget(Object[] params) {
			return false;
		}

		@Override
		public void handleCallFromServer(String method, Object[] params) {
			if ("refresh".equals(method)) {
				refresh();
			}
		}
	}
}
