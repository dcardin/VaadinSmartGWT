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

import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.grid.ListGrid;
import org.vaadin.smartgwt.server.grid.ListGridRecord;

import com.google.web.bindery.event.shared.Event;

public class SelectionEvent extends Event<SelectionChangedHandler> {
	private static Event.Type<SelectionChangedHandler> TYPE;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Event.Type<SelectionChangedHandler> getType() {
		return TYPE == null ? TYPE = new Event.Type<SelectionChangedHandler>() : TYPE;
	}

	private final Record record;
	private final boolean state;
	private final ListGridRecord[] selection;
	private final ListGridRecord selectedRecord;

	public SelectionEvent(Record record, boolean state, ListGridRecord[] selection, ListGridRecord selectedRecord) {
		this.record = record;
		this.state = state;
		this.selection = selection;
		this.selectedRecord = selectedRecord;
	}

	@Override
	public Event.Type<SelectionChangedHandler> getAssociatedType() {
		return getType();
	}

	@Override
	protected void dispatch(SelectionChangedHandler handler) {
		handler.onSelectionChanged(this);
	}

	/**
	 * record for which selection changed
	 *
	 * @return record for which selection changed
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * New selection state (true for selected, false for unselected)
	 *
	 * @return New selection state (true for selected, false for unselected)
	 */
	public boolean getState() {
		return state;
	}

	/**
	 * The selection associated with the listGrid. Alias for {@link #getRecord()}
	 *
	 * @return the selection
	 */
	public ListGridRecord[] getSelection() {
		return selection;
	}

	/**
	 * Return the first selected record in this component.<br><br> This method is appropriate if <code>{@link
	 * ListGrid#getSelectionType selectionType}</code> is <code>"single"</code>, or if you
	 * only care about the first selected record in a multiple-record selection. To access all selected records, use
	 * <code>{@link ListGrid#getSelection ListGrid.getSelection}</code> instead.
	 *
	 * @return first selected record, or null if nothing selected
	 */
	public ListGridRecord getSelectedRecord() {
		return selectedRecord;
	}
}
