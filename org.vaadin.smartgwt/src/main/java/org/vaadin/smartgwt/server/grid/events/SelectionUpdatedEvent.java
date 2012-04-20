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
package org.vaadin.smartgwt.server.grid.events;

import com.google.web.bindery.event.shared.Event;

public class SelectionUpdatedEvent extends Event<SelectionUpdatedHandler> {
	private static Type<SelectionUpdatedHandler> TYPE;

	public static Type<SelectionUpdatedHandler> getType() {
		return TYPE == null ? TYPE = new Type<SelectionUpdatedHandler>() : TYPE;
	}

	@Override
	public Type<SelectionUpdatedHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(SelectionUpdatedHandler handler) {
		handler.onSelectionUpdated(this);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SelectionUpdatedEvent;
	}

	@Override
	public int hashCode() {
		return 1;
	}
}
