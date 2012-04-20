package org.vaadin.smartgwt.server.grid;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.grid.events.SelectionChangedHandler;
import org.vaadin.smartgwt.server.grid.events.SelectionEvent;

import argo.jdom.JsonNode;

import com.google.common.collect.Maps;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class ListGridTest {
	private ListGrid listGrid;
	private SelectionEventFactory selectionEventFactory;

	@Before
	public void before() {
		listGrid = new ListGrid();
		listGrid.setSelectionEventFactory(selectionEventFactory = mock(SelectionEventFactory.class));
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
	public void test_firesSelectionChangeEventWhenVariablePresent() {
		final HashMap<String, Object> variables = Maps.<String, Object> newHashMap();
		variables.put("onSelectionChanged.event", "{}");

		final SelectionEvent event = mock(SelectionEvent.class);
		when(selectionEventFactory.newSelectionEvent(any(JsonNode.class))).thenReturn(event);

		final SelectionChangedHandler handler = mock(SelectionChangedHandler.class);
		listGrid.addSelectionChangedHandler(handler);

		listGrid.changeVariables(null, variables);
		verify(handler).onSelectionChanged(event);
	}
}
