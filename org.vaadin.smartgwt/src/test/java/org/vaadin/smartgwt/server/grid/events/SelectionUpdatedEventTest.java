package org.vaadin.smartgwt.server.grid.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class SelectionUpdatedEventTest {
	@Test
	public void test_typePropertyReturnsSingleton() {
		assertSame(SelectionUpdatedEvent.getType(), SelectionUpdatedEvent.getType());
	}

	private SelectionUpdatedEvent event;
	private Object source;

	@Before
	public void before() {
		event = new SelectionUpdatedEvent(source = new Object());
	}

	@Test
	public void test_sourceProperty() {
		assertEquals(source, event.getSource());
	}

	@Test
	public void test_associatedTypeIsSameAsTypeSingleton() {
		assertSame(SelectionUpdatedEvent.getType(), event.getAssociatedType());
	}

	@Test
	public void test_dispatchesEventToHandler() {
		final SelectionUpdatedHandler handler = mock(SelectionUpdatedHandler.class);
		event.dispatch(handler);
		verify(handler).onSelectionUpdated(event);
	}

	@Test
	public void test_equality() {
		assertEquals(new SelectionUpdatedEvent(source), event);
	}

	@Test
	public void test_hashCodeEquality() {
		assertEquals(new SelectionUpdatedEvent(source).hashCode(), event.hashCode());
	}
}
