package org.vaadin.smartgwt.server.extra;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class RefresherTest {
	private Refresher refresher;

	@Before
	public void before() throws Exception {
		Refresher.class.getClassLoader().setClassAssertionStatus(Refresher.class.getName(), true);
		refresher = (Refresher) Refresher.class.getClassLoader().loadClass(Refresher.class.getName()).newInstance();
	}

	@Test
	public void test_hasADefaultIntervalOfOneSecond() {
		assertEquals(1000, refresher.getInterval());
	}

	@Test
	public void test_canSetInterval() {
		refresher.setInterval(3000);
		assertEquals(3000, refresher.getInterval());
	}

	@Test(expected = AssertionError.class)
	public void test_assertsIntervalIsAPositiveNumber() {
		refresher.setInterval(-10);
	}

	@Test
	public void test_canAddARefreshListener() {
		final RefreshListener listener = mock(RefreshListener.class);

		refresher.addListener(listener);
		refresher.changeVariables(null, null);
		verify(listener).refresh(refresher);
	}
	
	@Test
	public void test_canRemoveRefreshListenerWithRegistrationHandler() {
		final RefreshListener listener = mock(RefreshListener.class);

		refresher.addListener(listener).removeHandler();
		refresher.changeVariables(null, null);
		verify(listener, never()).refresh(refresher);
	}

	@Test
	public void test_paintsInterval() throws PaintException {
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);

		refresher.setInterval(1000);
		refresher.addListener(mock(RefreshListener.class));
		refresher.paintContent(paintTarget);
		verify(paintTarget).addAttribute("interval", 1000);
	}

	@Test
	public void test_doesNotPaintIntervalWhenNoListenersAreRegistered() throws PaintException {
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);

		refresher.setInterval(1000);
		refresher.paintContent(paintTarget);
		verify(paintTarget, never()).addAttribute("interval", 1000);
	}
}
