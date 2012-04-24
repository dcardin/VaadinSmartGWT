package org.vaadin.smartgwt.server.data;

import java.util.List;

import javax.inject.Inject;

import argo.jdom.JsonNode;

/**
 * builds Record instances. 
 */
public class RecordFactory {
	private final RecordJSONUpdater updater;

	@Inject
	public RecordFactory(RecordJSONUpdater updater) {
		this.updater = updater;
	}

	/**
	 * builds a Record instance from a JsonNode.
	 * 
	 * @param node the source json node.
	 * @return a new record instance base on the JsonNode information, or null if the node referred to a null node.
	 */
	public Record newRecord(JsonNode node) {
		if (!node.isNullNode()) {
			final Record record = new Record();
			updater.update(record, node);
			return record;
		} else {
			return null;
		}
	}

	/**
	 * builds a Record matched size array from a List of JsonNode. 
	 * 
	 * @param nodes source list of JsonNodes.
	 * @return a new record array base on the JsonNode list information.
	 */
	public Record[] newRecords(List<JsonNode> nodes) {
		final Record[] records = new Record[nodes.size()];
		for (int i = 0; i < records.length; i++) {
			records[i] = newRecord(nodes.get(i));
		}
		return records;
	}
}