package org.vaadin.smartgwt.server;

import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.core.RegistrationEntry;
import org.vaadin.smartgwt.server.layout.MasterContainer;

import com.google.common.collect.Maps;

public class WindowTest {
	private Window window;
	private MasterContainer container;
	private RegistrationEntry registrationEntry;

	@Before
	public void before() {
		window = new Window(container = mock(MasterContainer.class));
		when(container.register(window)).thenReturn(registrationEntry = mock(RegistrationEntry.class));
		when(registrationEntry.isRegistered()).thenReturn(true);
	}

	@Test
	public void test_registersToContainerWhenShowing() {
		window.show();
		verify(container).register(window);
	}

	@Test
	public void test_registersToContainerOnceWhenShowingMultipleTimes() {
		window.show();
		window.show();
		verify(container, times(1)).register(window);
	}

	@Test
	public void test_unregistersFromContainerOnDispose() {
		window.show();
		window.dispose();
		verify(registrationEntry).unregister();
	}

	@Test
	public void test_unregistersFromContainerWhenClientSideDestroys() {
		final HashMap<String, Object> variables = Maps.newHashMap();
		variables.put("destroyed", true);

		window.show();
		window.changeVariables(null, variables);
		verify(registrationEntry).unregister();
	}
}
