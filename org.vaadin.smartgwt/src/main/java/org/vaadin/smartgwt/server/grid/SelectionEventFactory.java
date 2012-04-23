package org.vaadin.smartgwt.server.grid;

import javax.inject.Inject;

import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.data.RecordFactory;
import org.vaadin.smartgwt.server.grid.events.SelectionEvent;

import argo.jdom.JsonNode;

public class SelectionEventFactory {
	private final RecordFactory recordFactory;
	private final ListGridRecordFactory listGridRecordFactory;

	@Inject
	public SelectionEventFactory(RecordFactory recordFactory, ListGridRecordFactory listGridRecordFactory) {
		this.recordFactory = recordFactory;
		this.listGridRecordFactory = listGridRecordFactory;
	}

	public SelectionEvent newSelectionEvent(JsonNode node) {
		final Record record = recordFactory.newRecord(node.getNode("record"));
		final boolean state = node.getBooleanValue("state");
		final ListGridRecord[] selection = listGridRecordFactory.newListGridRecords(node.getArrayNode("selection"));
		final ListGridRecord selectedRecord = listGridRecordFactory.newListGridRecord(node.getNode("selectedRecord"));
		return new SelectionEvent(record, state, selection, selectedRecord);
	}
}