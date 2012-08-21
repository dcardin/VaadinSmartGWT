package org.vaadin.smartgwt.client.util;

import org.vaadin.rpc.client.ClientSideHandler;
import org.vaadin.rpc.client.ClientSideProxy;
import org.vaadin.rpc.shared.Method;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSC extends Widget implements Paintable {
	private final Element element = DOM.createDiv();
	protected String paintableId;
	private ApplicationConnection client;
	private final ClientSideProxy rpc = new ClientSideProxy("VSC", new ClientSideHandlerImpl());

	public VSC() {
		rpc.register("sayNoCallback", new Method() {
			public void invoke(final String methodName, final Object[] data) {
				if (data.length == 1) {
					SC.say((String) data[0]);
				} else {
					SC.say((String) data[0], (String) data[1]);
				}
			}
		});

		rpc.register("sayWithCallback", new Method() {
			public void invoke(final String methodName, final Object[] data) {
				if (data.length == 1) {
					SC.say((String) data[0], new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							client.updateVariable(paintableId, "callback", value != null ? value : false, true);
						}
					});
				} else {
					SC.say((String) data[0], (String) data[1], new BooleanCallback() {
						@Override
						public void execute(Boolean value) {
							client.updateVariable(paintableId, "callback", value != null ? value : false, true);
						}
					});
				}
			}
		});

		rpc.register("confirm", new Method() {
			@Override
			public void invoke(String methodName, Object[] params) {
				final int key = (Integer) params[0];
				final String message = (String) params[1];
				final BooleanCallback callback = new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						client.updateVariable(paintableId, "callbackKey", key, false);

						if (value == null) {
							client.updateVariable(paintableId, "callback", "null", true);
						} else {
							client.updateVariable(paintableId, "callback", value, true);
						}
					}
				};

				if (params.length < 2) {
					SC.confirm(message, callback);
				} else {
					SC.confirm((String) params[2], message, callback);
				}
			}
		});

		rpc.register("ask", new Method() {
			public void invoke(final String methodName, final Object[] data) {
				SC.ask((String) data[1], (String) data[2], new BooleanCallback() {
					@Override
					public void execute(Boolean value) {
						client.updateVariable(paintableId, "callbackKey", (Integer) data[0], false);

						if (value == null) {
							client.updateVariable(paintableId, "callback", "null", true);
						} else {
							client.updateVariable(paintableId, "callback", value, true);
						}
					}
				});
			}
		});
	}

	@Override
	public Element getElement() {
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		rpc.update(this, uidl, client);

		this.client = client;
		paintableId = uidl.getId();
	}

	private class ClientSideHandlerImpl implements ClientSideHandler {
		@Override
		public boolean initWidget(Object[] params) {
			return false;
		}

		@Override
		public void handleCallFromServer(String method, Object[] params) {

		}
	}
}
