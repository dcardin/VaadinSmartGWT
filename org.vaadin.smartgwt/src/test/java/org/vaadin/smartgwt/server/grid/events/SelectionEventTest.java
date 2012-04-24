package org.vaadin.smartgwt.server.grid.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.data.Record;
import org.vaadin.smartgwt.server.grid.ListGridRecord;

public class SelectionEventTest {
	@Test
	public void test_returnsSingletonType() {
		assertSame(SelectionEvent.getType(), SelectionEvent.getType());
	}

	private SelectionEvent selectionEvent;
	private Record record;
	private boolean state;
	private ListGridRecord[] selection;
	private ListGridRecord selectedRecord;

	@Before
	public void before() {
		selectionEvent = new SelectionEvent(record = new Record(), state = false, selection = new ListGridRecord[0], selectedRecord = new ListGridRecord());
	}
	
	@Test
	public void test_associatedTypePropertyReturnsSingletonType() {
		assertSame(SelectionEvent.getType(), selectionEvent.getAssociatedType());
	}

	@Test
	public void test_dispatchingEventToHandler() {
		final SelectionChangedHandler handler = mock(SelectionChangedHandler.class);
		selectionEvent.dispatch(handler);
		verify(handler).onSelectionChanged(selectionEvent);
	}

	@Test
	public void test_recordProperty() {
		assertEquals(record, selectionEvent.getRecord());
	}

	@Test
	public void test_stateProperty() {
		assertEquals(state, selectionEvent.getState());
	}

	@Test
	public void test_selectionProperty() {
		assertArrayEquals(selection, selectionEvent.getSelection());
	}

	@Test
	public void test_selectedRecordProperty() {
		assertEquals(selectedRecord, selectionEvent.getSelectedRecord());
	}
}
