package org.vaadin.smartgwt.client.ui;

import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.ServerSideEventRegistration;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.events.CloseClientEvent;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VWindow extends Window implements Paintable {
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();
	private final Element element = DOM.createDiv();
	private String pid;
	private ApplicationConnection client;
	private ServerSideEventRegistration closeClickEventServerRegistration;

	public VWindow() {
		propertyUpdater.addPaintableListListener("members", new PaintableListListener() {
			@Override
			public void onAdd(Paintable[] source, Integer index, Paintable element) {
				if (index == null) {
					addMember((Canvas) element);
				} else {
					addMember((Canvas) element, index);
				}
			}

			@Override
			public void onRemove(Paintable[] source, Integer index, Paintable element) {
				removeMember((Canvas) element);
			}
		});

		propertyUpdater.addPaintableListListener("items", new PaintableListListener() {
			@Override
			public void onAdd(Paintable[] source, Integer index, Paintable element) {
				addItem((Canvas) element);
			}

			@Override
			public void onRemove(Paintable[] source, Integer index, Paintable element) {
				removeItem((Canvas) element);
			}
		});
	}

	@Override
	public Element getElement() {
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		if (this.pid == null) {
			this.pid = uidl.getId();
			this.client = client;

			addCloseClickHandler(new CloseClickHandler() {
				@Override
				public void onCloseClick(CloseClientEvent event) {
					markForDestroy();
					VWindow.this.client.updateVariable(pid, "destroyed", true, true);
				}
			});
			
			closeClickEventServerRegistration = new ServerSideEventRegistration("*hasCloseClickHandlers") {
				@Override
				protected HandlerRegistration registerHandler() {
					return addCloseClickHandler(new CloseClickHandler() {
						@Override
						public void onCloseClick(CloseClientEvent event) {
							VWindow.this.client.updateVariable(pid, "onCloseClick", true, true);
						}
					});
				}
			};
		}

		closeClickEventServerRegistration.updateFromUIDL(uidl);
		propertyUpdater.updateFromUIDL(uidl, client);
		PainterHelper.updateSmartGWTComponent(client, this, uidl);
	}
}