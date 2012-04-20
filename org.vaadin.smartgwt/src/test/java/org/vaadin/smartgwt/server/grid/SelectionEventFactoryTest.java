package org.vaadin.smartgwt.server.grid;

import static argo.jdom.JsonNodeFactories.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.data.RecordFactory;
import org.vaadin.smartgwt.server.grid.events.SelectionEvent;

import argo.jdom.JsonRootNode;

public class SelectionEventFactoryTest {
	private SelectionEventFactory selectionEventFactory;
	private RecordFactory recordFactory;
	private ListGridRecordFactory listGridRecordFactory;

	@Before
	public void before() {
		recordFactory = mock(RecordFactory.class);
		listGridRecordFactory = mock(ListGridRecordFactory.class);
		selectionEventFactory = new SelectionEventFactory(recordFactory, listGridRecordFactory);
	}

	@Test
	public void test_configuresRecordAttribute() {
		final JsonRootNode node = buildJSON();
		when(recordFactory.newRecord(node.getNode("record"))).thenReturn(new Record());

		final SelectionEvent event = selectionEventFactory.newSelectionEvent(node);
		assertEquals(recordFactory.newRecord(node.getNode("record")), event.getRecord());
	}

	@Test
	public void test_configuresStateAttribute() {
		final JsonRootNode node = buildJSON();

		final SelectionEvent event = selectionEventFactory.newSelectionEvent(node);
		assertEquals(node.getBooleanValue("state"), event.getState());
	}
	
	@Test
	public void test_configuresSelectionAttribute() {
		final JsonRootNode node = buildJSON();
		when(listGridRecordFactory.newListGridRecords(node.getArrayNode("selection"))).thenReturn(new ListGridRecord[0]);

		final SelectionEvent event = selectionEventFactory.newSelectionEvent(node);
		assertArrayEquals(listGridRecordFactory.newListGridRecords(node.getArrayNode("selection")), event.getSelection());
	}

	@Test
	public void test_configuresSelectedRecordAttribute() {
		final JsonRootNode node = buildJSON();
		when(listGridRecordFactory.newListGridRecord(node.getNode("selectedRecord"))).thenReturn(new ListGridRecord());

		final SelectionEvent event = selectionEventFactory.newSelectionEvent(node);
		assertEquals(listGridRecordFactory.newListGridRecord(node.getNode("selectedRecord")), event.getSelectedRecord());
	}

	// @formatter:off
	private static JsonRootNode buildJSON() {
		return aJsonObject(
			aJsonField("record", aJsonObject()),
			aJsonField("state", aJsonBoolean(true)),
			aJsonField("selection", aJsonArray()),
			aJsonField("selectedRecord", aJsonObject())
		);
	}
	// @formatter:on
}
