package org.vaadin.smartgwt.server.grid.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.grid.ListGrid;
import org.vaadin.smartgwt.server.grid.ListGridField;

public class RecordDoubleClickEventTest {
	@Test
	public void test_typePropertyIsSingleton() {
		assertSame(RecordDoubleClickEvent.getType(), RecordDoubleClickEvent.getType());
	}

	private RecordDoubleClickEvent event;
	private Object source;
	private ListGrid viewer;
	private Record record;
	private int recordNum;
	private ListGridField field;
	private int fieldNum;

	@Before
	public void before() {
		source = new Object();
		viewer = new ListGrid();
		record = new Record();
		recordNum = 1;
		field = new ListGridField();
		fieldNum = 2;
		event = new RecordDoubleClickEvent(source, viewer, record, recordNum, field, fieldNum);
	}

	@Test
	public void test_associatedTypeIsSameAsSingletonType() {
		assertSame(RecordDoubleClickEvent.getType(), event.getAssociatedType());
	}

	@Test
	public void test_sourceProperty() {
		assertEquals(source, event.getSource());
	}

	@Test
	public void test_viewerProperty() {
		assertEquals(viewer, event.getViewer());
	}

	@Test
	public void test_recordProperty() {
		assertEquals(record, event.getRecord());
	}

	@Test
	public void test_recordNumProperty() {
		assertEquals(recordNum, event.getRecordNum());
	}

	@Test
	public void test_fieldProperty() {
		assertEquals(field, event.getField());
	}

	@Test
	public void test_fieldNumProperty() {
		assertEquals(fieldNum, event.getFieldNum());
	}

	@Test
	public void test_dispatchesToHandler() {
		final RecordDoubleClickHandler handler = mock(RecordDoubleClickHandler.class);
		event.dispatch(handler);
		verify(handler).onRecordDoubleClick(event);
	}

	@Test
	public void test_equality() {
		assertEquals(new RecordDoubleClickEvent(source, viewer, record, recordNum, field, fieldNum), event);
	}

	@Test
	public void test_hashcodeEquality() {
		assertEquals(new RecordDoubleClickEvent(source, viewer, record, recordNum, field, fieldNum).hashCode(), event.hashCode());
	}
}
