/*
 * SmartGWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.vaadin.smartgwt.server.util;

import java.util.Map;
import java.util.Stack;

import org.vaadin.rpc.server.ServerSideHandler;
import org.vaadin.rpc.server.ServerSideProxy;
import org.vaadin.smartgwt.server.Canvas;

import com.google.common.collect.Maps;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.util.VSC.class)
public class SC extends Canvas {
	static class ServerSideProxyFactory {
		public ServerSideProxy newServerSideProxy(ServerSideHandler handler) {
			return new ServerSideProxy(handler);
		}
	}

	private static class ConcurrentIncrementor {
		private int next = 0;

		public synchronized int increment() {
			return next = next + 1;
		}
	}

	private final ServerSideProxy client;
	private final ConcurrentIncrementor incrementor = new ConcurrentIncrementor();
	private final Map<Integer, BooleanCallback> callbacks = Maps.newHashMap();
	private Stack callBacks = new Stack();

	public SC() {
		this(new ServerSideProxyFactory());
	}

	SC(ServerSideProxyFactory serverSideProxyFactory) {
		client = serverSideProxyFactory.newServerSideProxy(new ServerSideHandlerImpl());
	}

	public void say(String message) {
		client.call("sayNoCallback", message);
	}

	public void say(String message, BooleanCallback bcb) {
		client.call("sayWithCallback", message);
		callBacks.push(bcb);
	}

	public void say(String title, String message) {
		client.call("sayNoCallback", title, message);
	}

	public void say(String title, String message, BooleanCallback bcb) {
		client.call("sayWithCallback", title, message);
		callBacks.push(bcb);
	}

	/**
	 * Show a modal dialog with a message, icon, and "OK" and "Cancel" buttons.
	 * <p>
	 * The callback will receive boolean true for an OK button click, or null for a Cancel click or if the Dialog is dismissed via the close button.
	 *
	 * @param message message to display
	 * @param callback Callback to fire when the user clicks a button to dismiss the dialog.
	 */
	public void confirm(String message, BooleanCallback callback) {
		confirm(null, message, callback);
	}

	/**
	 * Show a modal dialog with a message, icon, and "OK" and "Cancel" buttons.
	 * <p>
	 * The callback will receive boolean true for an OK button click, or null for a Cancel click or if the Dialog is dismissed via the close button.
	 *
	 * @param title the title of the dialog
	 * @param message message to display
	 * @param callback Callback to fire when the user clicks a button to dismiss the dialog.
	 */
	public void confirm(String title, String message, BooleanCallback callback) {
		final int key = incrementor.increment();
		client.call("confirm", key, message, title);
		callbacks.put(key, callback);
	}

	/**
	* Show a modal dialog with a message, icon, and "Yes" and "No" buttons. The callback will receive boolean true for an OK
	* button click, boolean false for a No button click, or null if the Dialog is dismissed via the close button.
	* 
	* @param message the message
	* @param callback the callback to fire when the user dismisses the dialog.
	*/
	public void ask(String message, BooleanCallback callback) {
		ask(null, message, callback);
	}

	/**
	 * Show a modal dialog with a message, icon, and "Yes" and "No" buttons. The callback will receive boolean true for an OK
	 * button click, boolean false for a No button click, or null if the Dialog is dismissed via the close button.
	 *
	 * @param title the title of the message box
	 * @param message the message
	 * @param callback the callback to fire when the user dismisses the dialog.
	 */
	public void ask(String title, String message, BooleanCallback callback) {
		final int key = incrementor.increment();
		client.call("ask", key, message, title);
		callbacks.put(key, callback);
	}

	@Override
	public void changeVariables(final Object source, final Map variables) {
		client.changeVariables(source, variables);
		if (callBacks.size() > 0) {
			BooleanCallback bcp = (BooleanCallback) callBacks.pop();
			bcp.execute((Boolean) variables.get("callback"));
		}

		if (variables.containsKey("callbackKey")) {
			final BooleanCallback callback = callbacks.get(variables.get("callbackKey"));
			callback.execute("null".equals(variables.get("callback")) ? null : (Boolean) variables.get("callback"));
		}
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		super.paintContent(target);
		client.paintContent(target);
	}

	private class ServerSideHandlerImpl implements ServerSideHandler {
		@Override
		public Object[] initRequestFromClient() {
			return new Object[0];
		}

		@Override
		public void callFromClient(String method, Object[] params) {

		}

		@Override
		public void requestRepaint() {
			SC.this.requestRepaint();
		}
	}
}
