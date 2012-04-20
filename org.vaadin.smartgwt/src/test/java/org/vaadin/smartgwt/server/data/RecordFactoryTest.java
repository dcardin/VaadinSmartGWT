package org.vaadin.smartgwt.server.data;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import argo.jdom.JsonNode;

import com.google.common.collect.Lists;

public class RecordFactoryTest {
	private RecordFactory recordFactory;
	private RecordJSONUpdater updater;

	@Before
	public void before() {
		recordFactory = new RecordFactory(updater = mock(RecordJSONUpdater.class));
	}

	@Test
	public void test_newRecord() {
		assertTrue(recordFactory.newRecord(null) != null);
	}

	@Test
	public void test_callsUpdaterWhenCreatingRecord() {
		final JsonNode node = mock(JsonNode.class);
		final Record record = recordFactory.newRecord(node);
		verify(updater).update(record, node);
	}

	@Test
	public void test_createsRecordsArrayMatchingSizeOfNodesList() {
		final ArrayList<JsonNode> nodes = Lists.newArrayList(mock(JsonNode.class));
		assertEquals(nodes.size(), recordFactory.newRecords(nodes).length);
	}

	@Test
	public void test_callsUpdaterOnRecordsArrayCreation() {
		final ArrayList<JsonNode> nodes = Lists.newArrayList(mock(JsonNode.class), mock(JsonNode.class));
		final Record[] records = recordFactory.newRecords(nodes);
		verify(updater).update(records[0], nodes.get(0));
		verify(updater).update(records[1], nodes.get(1));
	}
}
