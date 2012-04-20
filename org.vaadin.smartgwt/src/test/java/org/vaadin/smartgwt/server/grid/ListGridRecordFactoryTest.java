package org.vaadin.smartgwt.server.grid;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.data.RecordJSONUpdater;

import argo.jdom.JsonNode;

import com.google.common.collect.Lists;

public class ListGridRecordFactoryTest {
	private ListGridRecordFactory recordFactory;
	private RecordJSONUpdater updater;

	@Before
	public void before() {
		recordFactory = new ListGridRecordFactory(updater = mock(RecordJSONUpdater.class));
	}

	@Test
	public void test_newRecord() {
		assertTrue(recordFactory.newListGridRecord(null) != null);
	}

	@Test
	public void test_callsUpdaterWhenCreatingRecord() {
		final JsonNode node = mock(JsonNode.class);
		final ListGridRecord record = recordFactory.newListGridRecord(node);
		verify(updater).update(record, node);
	}

	@Test
	public void test_createsRecordsArrayMatchingSizeOfNodesList() {
		final ArrayList<JsonNode> nodes = Lists.newArrayList(mock(JsonNode.class));
		assertEquals(nodes.size(), recordFactory.newListGridRecords(nodes).length);
	}

	@Test
	public void test_callsUpdaterOnRecordsArrayCreation() {
		final ArrayList<JsonNode> nodes = Lists.newArrayList(mock(JsonNode.class), mock(JsonNode.class));
		final ListGridRecord[] records = recordFactory.newListGridRecords(nodes);
		verify(updater).update(records[0], nodes.get(0));
		verify(updater).update(records[1], nodes.get(1));
	}
}
