package org.vaadin.smartgwt.server.grid;

import java.util.List;

import javax.inject.Inject;

import org.vaadin.smartgwt.server.data.RecordJSONUpdater;

import argo.jdom.JsonNode;

/**
 * builds ListGridRecord instances. 
 */
public class ListGridRecordFactory {
	private final RecordJSONUpdater updater;

	@Inject
	public ListGridRecordFactory(RecordJSONUpdater updater) {
		this.updater = updater;
	}

	/**
	 * builds a new ListGridRecord from the JsonNode attributes.
	 * 
	 * @param node containing ListGridRecord attributes.
	 * @return a new ListGridRecord built from the JsonNode attributes.
	 */
	public ListGridRecord newListGridRecord(JsonNode node) {
		if (!node.isNullNode()) {
			final ListGridRecord record = new ListGridRecord();
			updater.update(record, node);
			return record;
		} else {
			return null;
		}
	}

	/**
	 * builds a new array of ListGridRecords from the JsonNodes attributes.
	 * 
	 * @param nodes containing ListGridRecords attributes.
	 * @return a new array of ListGridRecords built from the JsonNodes attributes.
	 */
	public ListGridRecord[] newListGridRecords(List<JsonNode> nodes) {
		final ListGridRecord[] records = new ListGridRecord[nodes.size()];
		for (int i = 0; i < records.length; i++) {
			records[i] = newListGridRecord(nodes.get(i));
		}
		return records;
	}
}