package org.vaadin.smartgwt.server.data;

import java.util.List;

import javax.inject.Inject;

import argo.jdom.JsonNode;

public class RecordFactory {
	private final RecordJSONUpdater updater;

	@Inject
	public RecordFactory(RecordJSONUpdater updater) {
		this.updater = updater;
	}

	public Record newRecord(JsonNode node) {
		final Record record = new Record();
		updater.update(record, node);
		return record;
	}

	public Record[] newRecords(List<JsonNode> nodes) {
		final Record[] records = new Record[nodes.size()];
		for (int i = 0; i < records.length; i++) {
			updater.update(records[i], nodes.get(i));
		}
		return records;
	}
}