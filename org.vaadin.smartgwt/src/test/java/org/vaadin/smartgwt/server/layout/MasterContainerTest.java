package org.vaadin.smartgwt.server.layout;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.core.RegistrationEntry;

import com.google.common.collect.ImmutableList;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.Paintable.RepaintRequestEvent;
import com.vaadin.terminal.Paintable.RepaintRequestListener;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

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

	@Test
	public void test_addNonUIComponent() {
		final NonUIComponent nonUIComponent = mock(NonUIComponent.class);

		container.addNonUIComponent(nonUIComponent);
		assertTrue(ImmutableList.copyOf(container.getNonUIComponentIterator()).contains(nonUIComponent));
	}

	@Test
	public void test_removeNonUIComponent() {
		final NonUIComponent nonUIComponent = mock(NonUIComponent.class);
		container.addNonUIComponent(nonUIComponent);

		container.removeNonUIComponent(nonUIComponent);
		assertFalse(ImmutableList.copyOf(container.getNonUIComponentIterator()).contains(nonUIComponent));
	}

	@Test
	public void test_paintsNonUIComponent() throws PaintException {
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);
		final NonUIComponent nonUIComponent = mock(NonUIComponent.class);
		
		container.addNonUIComponent(nonUIComponent);
		container.paintContent(paintTarget);
		
		final InOrder inOrder = inOrder(paintTarget, nonUIComponent);
		inOrder.verify(paintTarget).startTag("$nonUIComponents");
		inOrder.verify(nonUIComponent).paint(paintTarget);
		inOrder.verify(paintTarget).endTag("$nonUIComponents");
	}
}
