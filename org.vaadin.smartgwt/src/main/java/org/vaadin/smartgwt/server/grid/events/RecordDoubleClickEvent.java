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
import org.vaadin.smartgwt.server.grid.ListGridField;

import com.google.common.base.Objects;
import com.google.web.bindery.event.shared.Event;

public class RecordDoubleClickEvent extends Event<RecordDoubleClickHandler> {
	private static Event.Type<RecordDoubleClickHandler> TYPE;

	/**
	 * Gets the type associated with this event.
	 *
	 * @return returns the handler type
	 */
	public static Type<RecordDoubleClickHandler> getType() {
		return TYPE == null ? TYPE = new Event.Type<RecordDoubleClickHandler>() : TYPE;
	}

	private final ListGrid viewer;
	private final Record record;
	private final int recordNum;
	private final ListGridField field;
	private final int fieldNum;

	public RecordDoubleClickEvent(Object source, ListGrid viewer, Record record, int recordNum, ListGridField field, int fieldNum) {
		this.viewer = viewer;
		this.record = record;
		this.recordNum = recordNum;
		this.field = field;
		this.fieldNum = fieldNum;
		setSource(source);
	}

	@Override
	public Event.Type<RecordDoubleClickHandler> getAssociatedType() {
		return getType();
	}

	/**
	 * the listGrid that contains the doubleclick event
	 *
	 * @return the listGrid that contains the doubleclick event
	 */
	public ListGrid getViewer() {
		return viewer;
	}

	/**
	 * the record that was double-clicked
	 *
	 * @return the record that was double-clicked
	 */
	public Record getRecord() {
		return record;
	}

	/**
	 * number of the record clicked on in the current set of displayed records (starts with 0)
	 *
	 * @return number of the record clicked on in the current set of displayed records (starts with 0)
	 */
	public int getRecordNum() {
		return recordNum;
	}

	/**
	* the field that was clicked on (field definition)
	*
	* @return the field that was clicked on (field definition)
	*/
	public ListGridField getField() {
		return field;
	}
	
	/**
	 * number of the field clicked on in the listGrid.fields array
	 *
	 * @return number of the field clicked on in the listGrid.fields array
	 */
	public int getFieldNum() {
		return fieldNum;
    }

	@Override
	protected void dispatch(RecordDoubleClickHandler handler) {
		handler.onRecordDoubleClick(this);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RecordDoubleClickEvent) {
			final RecordDoubleClickEvent event = (RecordDoubleClickEvent) obj;
			boolean result = true;
			result = result && Objects.equal(getSource(), event.getSource());
			result = result && Objects.equal(viewer, event.viewer);
			result = result && Objects.equal(record, event.record);
			result = result && Objects.equal(recordNum, event.recordNum);
			result = result && Objects.equal(field, event.field);
			result = result && Objects.equal(fieldNum, event.fieldNum);
			return result;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getSource(), viewer, record, recordNum, field, fieldNum);
	}
}
