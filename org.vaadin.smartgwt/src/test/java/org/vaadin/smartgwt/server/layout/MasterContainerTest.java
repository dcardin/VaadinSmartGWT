package org.vaadin.smartgwt.server.layout;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.core.RegistrationEntry;

import com.vaadin.terminal.Paintable.RepaintRequestEvent;
import com.vaadin.terminal.Paintable.RepaintRequestListener;

public class MasterContainerTest {
	private MasterContainer container;

	@Before
	public void before() {
		container = new MasterContainer();
	}

	@Test
	public void test_registerWindow() {
		final Window window = mock(Window.class);
		assertTrue(container.register(window).isRegistered());
	}

	@Test
	public void test_registeringWindowForcesRepaint() {
		final RepaintRequestListener listener = mock(RepaintRequestListener.class);
		final Window window = mock(Window.class);

		container.addListener(listener);
		container.register(window);
		verify(listener).repaintRequested(isA(RepaintRequestEvent.class));
	}

	@Test
	public void test_unregisterWindow() {
		final Window window = mock(Window.class);
		final RegistrationEntry registration = container.register(window);

		registration.unregister();
		assertFalse(registration.isRegistered());
	}

	@Test
	public void test_unregisteringWindowForcesRepaint() {
		final RepaintRequestListener listener = mock(RepaintRequestListener.class);
		final Window window = mock(Window.class);

		final RegistrationEntry registration = container.register(window);
		container.addListener(listener);
		registration.unregister();
		verify(listener).repaintRequested(isA(RepaintRequestEvent.class));
	}
}
