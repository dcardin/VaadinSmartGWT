/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.vaadin.smartgwt.server.events;

import com.google.web.bindery.event.shared.Event;

public class ClickEvent extends Event<ClickHandler> {
	private static Event.Type<ClickHandler> TYPE;

	public static Event.Type<ClickHandler> getType() {
		return TYPE == null ? TYPE = new Event.Type<ClickHandler>() : TYPE;
	}

	@Override
	public Event.Type<ClickHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(ClickHandler handler) {
		handler.onClick(this);
	}
}
