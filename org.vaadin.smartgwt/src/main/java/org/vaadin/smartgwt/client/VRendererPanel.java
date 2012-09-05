package org.vaadin.smartgwt.client;

import java.util.Date;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.AnimationEffect;
import com.smartgwt.client.widgets.AnimationCallback;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VRendererPanel extends VLayout implements Paintable {
	private Img main;

	private final ClientSideProxy rpc = new ClientSideProxy("VRendererPanel", new ClientSideHandlerImpl());
	private String pid;
	private Image img = new Image();
	private String url;
	private int height;
	private int width;

	public VRendererPanel() {
		main = new Img();
		main.setHeight("99%");
		main.setWidth("99%");
		main.setAnimateShowTime(1000);
		main.setAnimateHideTime(1000);

		addMember(main);

		addResizedHandler(new ResizedHandler() {
			@Override
			public void onResized(ResizedEvent event) {
				if (getWidth() != width || getHeight() != height) {
					height = getHeight();
					width = getWidth();
					refresh();
				}
			}
		});

		img.setVisible(false);
		RootPanel.get().add(img);

		img.addLoadHandler(new LoadHandler() {
			@Override
			public void onLoad(LoadEvent event) {
				main.animateHide(AnimationEffect.FADE, new AnimationCallback() {
					@Override
					public void execute(boolean earlyFinish) {
						main.setSrc(url);
						main.animateShow(AnimationEffect.FADE);
					}
				});
			};
		});

		oneTimeTimer();
	}

	private void oneTimeTimer() {
		Timer t = new Timer() {
			public void run() {
				refresh();
			}
		};

		t.schedule(1000);
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
		url = GWT.getHostPageBaseURL() + "imageFetcher?type=main&width=" + width + "&height=" + height + "&time=" + date;

		img.setHeight(height);
		img.setWidth(width);
		img.setUrl(url);
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
