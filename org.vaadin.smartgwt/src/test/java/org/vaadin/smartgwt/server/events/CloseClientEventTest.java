package org.vaadin.smartgwt.server.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CloseClientEventTest {
	@Test
	public void test_returnsSameInstanceForType() {
		assertSame(CloseClientEvent.getType(), CloseClientEvent.getType());
	}

	private CloseClientEvent event;

	@Before
	public void before() {
		event = new CloseClientEvent();
	}

	@Test
	public void test_associatedTypeIsSameAsType() {
		assertSame(CloseClientEvent.getType(), event.getAssociatedType());
	}

	@Test
	public void test_dispatchesEventToHandler() {
		final CloseClickHandler handler = mock(CloseClickHandler.class);

		event.dispatch(handler);
		verify(handler).onCloseClick(event);
	}
}
