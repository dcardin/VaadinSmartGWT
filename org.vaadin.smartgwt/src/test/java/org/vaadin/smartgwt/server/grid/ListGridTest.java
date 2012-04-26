package org.vaadin.smartgwt.server.grid;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.grid.events.RecordDoubleClickEvent;
import org.vaadin.smartgwt.server.grid.events.RecordDoubleClickHandler;
import org.vaadin.smartgwt.server.grid.events.SelectionChangedHandler;
import org.vaadin.smartgwt.server.grid.events.SelectionEvent;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedEvent;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedHandler;

import argo.jdom.JsonNode;

import com.google.common.collect.Maps;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class ListGridTest {
	private ListGrid listGrid;
	private SelectionEventFactory selectionEventFactory;
	private JsonPaintTarget paintTarget;

	@Before
	public void before() {
		paintTarget = mock(JsonPaintTarget.class);
		listGrid = new ListGrid();
		listGrid.setSelectionEventFactory(selectionEventFactory = mock(SelectionEventFactory.class));
	}

	@Test
	public void test_addRecordDoubleClickHandler() {
		final RecordDoubleClickHandler handler = mock(RecordDoubleClickHandler.class);
		final RecordDoubleClickEvent event = newRecordDoubleClickEvent();
		listGrid.addRecordDoubleClickHandler(handler);
		listGrid.fireEvent(event);
		verify(handler).onRecordDoubleClick(event);
	}

	@Test
	public void test_removeRecordDoubleClickHandlerWithRegistration() {
		final RecordDoubleClickEvent event = newRecordDoubleClickEvent();
		final RecordDoubleClickHandler handler = mock(RecordDoubleClickHandler.class);
		final HandlerRegistration registration = listGrid.addRecordDoubleClickHandler(handler);

		registration.removeHandler();
		listGrid.fireEvent(event);
		verify(handler, never()).onRecordDoubleClick(event);
	}

	@Test
	public void test_doNotPaintRecordDoubleClickHandlerFlagWhenNoHandlersRegistered() throws PaintException {
		listGrid.paint(paintTarget);
		verify(paintTarget, never()).addAttribute("*hasRecordDoubleClickHandlers", true);
	}

	@Test
	public void test_paintRecordDoubleClickHandlerFlagWhenHandlersAreRegistered() throws PaintException {
		listGrid.addRecordDoubleClickHandler(mock(RecordDoubleClickHandler.class));
		listGrid.paint(paintTarget);
		verify(paintTarget).addAttribute("*hasRecordDoubleClickHandlers", true);
	}

	@Test
	public void test_selectionEventFactoryProperty() {
		final SelectionEventFactory selectionEventFactory = mock(SelectionEventFactory.class);
		listGrid.setSelectionEventFactory(selectionEventFactory);
		assertEquals(selectionEventFactory, listGrid.getSelectionEventFactory());
	}

	@Test
	public void test_doNotPaintSelectionChangedHandlerFlagWhenNoHandlersRegistered() throws PaintException {
		final JsonPaintTarget target = mock(JsonPaintTarget.class);
		listGrid.paintContent(target);
		verify(target, never()).addAttribute("*hasSelectionChangedHandlers", true);
	}

	@Test
	public void test_paintSelectionChangedHandlerFlagWhenRegisteredHandlers() throws PaintException {
		final JsonPaintTarget target = mock(JsonPaintTarget.class);
		listGrid.addSelectionChangedHandler(mock(SelectionChangedHandler.class));

		listGrid.paint(target);
		verify(target).addAttribute("*hasSelectionChangedHandlers", true);
	}

	@Test
	public void test_firesSelectionChangeEvent() {
		final HashMap<String, Object> variables = Maps.<String, Object> newHashMap();
		variables.put("onSelectionChanged.event", "{}");

		final SelectionEvent event = mock(SelectionEvent.class);
		when(selectionEventFactory.newSelectionEvent(any(JsonNode.class))).thenReturn(event);

		final SelectionChangedHandler handler = mock(SelectionChangedHandler.class);
		listGrid.addSelectionChangedHandler(handler);

		listGrid.changeVariables(null, variables);
		verify(handler).onSelectionChanged(event);
	}

	@Test
	public void test_canRemoveSelectionChangeEventFromRegistration() {
		final SelectionChangedHandler handler = mock(SelectionChangedHandler.class);
		final HandlerRegistration registration = listGrid.addSelectionChangedHandler(handler);

		registration.removeHandler();
		assertFalse(listGrid.getSelectionChangedHandlers().contains(handler));
	}

	@Test
	public void test_addSelectionUpdatedHandler() {
		final SelectionUpdatedHandler handler = mock(SelectionUpdatedHandler.class);
		listGrid.addSelectionUpdatedHandler(handler);
		assertTrue(listGrid.getSelectionUpdatedHandlers().contains(handler));
	}

	@Test
	public void test_removeSelectionUpdatedHandlerFromRegistration() {
		final SelectionUpdatedHandler handler = mock(SelectionUpdatedHandler.class);
		final HandlerRegistration registration = listGrid.addSelectionUpdatedHandler(handler);

		registration.removeHandler();
		assertFalse(listGrid.getSelectionUpdatedHandlers().contains(handler));
	}

	@Test
	public void test_paintSelectionUpdatedHandlerFlagWhenHandlersRegistered() throws PaintException {
		final JsonPaintTarget target = mock(JsonPaintTarget.class);
		listGrid.addSelectionUpdatedHandler(mock(SelectionUpdatedHandler.class));

		listGrid.paintContent(target);
		verify(target).addAttribute("*hasSelectionUpdatedHandlers", true);
	}

	@Test
	public void test_doNotPaintSelectionUpdatedHandlerFlagWhenNoRegisteredHandler() throws PaintException {
		final JsonPaintTarget target = mock(JsonPaintTarget.class);

		listGrid.paintContent(target);
		verify(target, never()).addAttribute("*hasSelectionUpdatedHandlers", true);
	}

	@Test
	public void test_firesSelectionUpdatedEvent() {
		final HashMap<String, Object> variables = Maps.<String, Object> newHashMap();
		variables.put("onSelectionUpdated.event", true);

		final SelectionUpdatedHandler handler = mock(SelectionUpdatedHandler.class);
		listGrid.addSelectionUpdatedHandler(handler);

		listGrid.changeVariables(null, variables);
		verify(handler).onSelectionUpdated(new SelectionUpdatedEvent(listGrid));
	}

	@Test
	public void test_updatesSelectedRecords() {
		final HashMap<String, Object> variables = Maps.<String, Object> newHashMap();
		variables.put("selectedRecords", "[]");

		final ListGridRecordFactory factory = mock(ListGridRecordFactory.class);
		listGrid.setListGridRecordFactory(factory);
		
		final ListGridRecord[] records = new ListGridRecord[0];
		when(factory.newListGridRecords(anyList())).thenReturn(records);

		listGrid.changeVariables(null, variables);
		assertArrayEquals(records, listGrid.getSelectedRecords());
	}

	@Test
	public void test_updatesSelectedRecordsBeforeFiringEvent() {
		final HashMap<String, Object> variables = Maps.<String, Object> newHashMap();
		variables.put("onSelectionUpdated.event", true);
		variables.put("selectedRecords", "[]");

		final ListGridRecordFactory factory = mock(ListGridRecordFactory.class);
		listGrid.setListGridRecordFactory(factory);

		final ListGridRecord[] records = new ListGridRecord[0];
		when(factory.newListGridRecords(anyList())).thenReturn(records);

		final CaptureSelectedRecords handler = new CaptureSelectedRecords();
		listGrid.addSelectionUpdatedHandler(handler);

		listGrid.changeVariables(null, variables);
		assertArrayEquals(records, handler.getSelectedRecords());
	}

	private static RecordDoubleClickEvent newRecordDoubleClickEvent() {
		return new RecordDoubleClickEvent(null, null, null, -1, null, -1);
	}

	private static class CaptureSelectedRecords implements SelectionUpdatedHandler {
		private ListGridRecord[] selectedRecords;

		@Override
		public void onSelectionUpdated(SelectionUpdatedEvent event) {
			selectedRecords = ((ListGrid) event.getSource()).getSelectedRecords();
		}

		public ListGridRecord[] getSelectedRecords() {
			return selectedRecords;
		}
	}
}
