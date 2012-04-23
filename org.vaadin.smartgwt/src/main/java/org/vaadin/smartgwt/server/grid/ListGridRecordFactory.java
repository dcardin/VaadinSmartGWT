package org.vaadin.smartgwt.server.grid;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.smartgwt.server.data.RecordJSONUpdater;

import argo.jdom.JsonNode;

public class ListGridRecordFactory {
	private final RecordJSONUpdater updater;

	@Inject
	public ListGridRecordFactory(RecordJSONUpdater updater) {
		this.updater = updater;
	}

	public ListGridRecord newListGridRecord(JsonNode node) {
		final ListGridRecord record = new ListGridRecord();
		updater.update(record, node);
		return record;
	}

	public ListGridRecord[] newListGridRecords(List<JsonNode> nodes) {
		final ListGridRecord[] records = new ListGridRecord[nodes.size()];
		for (int i = 0; i < records.length; i++) {
			updater.update(records[i], nodes.get(i));
		}
		return records;
	}
}