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

	@Before
	public void before() {
		event = new SelectionUpdatedEvent();
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
	public void test_equalityOnInstanceOf() {
		assertEquals(new SelectionUpdatedEvent(), event);
	}

	@Test
	public void test_hashCodeEquality() {
		assertEquals(new SelectionUpdatedEvent().hashCode(), event.hashCode());
	}
}
