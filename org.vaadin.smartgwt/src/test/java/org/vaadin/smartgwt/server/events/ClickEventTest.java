package org.vaadin.smartgwt.server.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class ClickEventTest {
	@Test
	public void test_alwaysReturnSameType() {
		assertEquals(ClickEvent.getType(), ClickEvent.getType());
	}

	private ClickEvent event;

	@Before
	public void before() {
		event = new ClickEvent();
	}

	@Test
	public void test_associatedTypeIsSameAsType() {
		assertEquals(ClickEvent.getType(), event.getAssociatedType());
	}

	@Test
	public void test_dispatchesEventToHandler() {
		final ClickHandler handler = mock(ClickHandler.class);

		event.dispatch(handler);
		verify(handler).onClick(event);
	}
}
