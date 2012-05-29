package org.vaadin.smartgwt.server.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class CloseClientEventTest {
	@Test
	public void test_returnsSameInstanceForType() {
		assertSame(CloseClickEvent.getType(), CloseClickEvent.getType());
	}

	private CloseClickEvent event;

	@Before
	public void before() {
		event = new CloseClickEvent();
	}

	@Test
	public void test_associatedTypeIsSameAsType() {
		assertSame(CloseClickEvent.getType(), event.getAssociatedType());
	}

	@Test
	public void test_dispatchesEventToHandler() {
		final CloseClickHandler handler = mock(CloseClickHandler.class);

		event.dispatch(handler);
		verify(handler).onCloseClick(event);
	}
}
