package org.vaadin.smartgwt.server;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class CanvasTest {
	private Canvas canvas;

	@Before
	public void before() {
		canvas = new Canvas();
	}

	@Test
	public void test_disabledProperty() {
		canvas.setDisabled(true);
		assertEquals(true, canvas.getDisabled());
	}

	@Test
	public void test_enabledProperty() {
		canvas.setEnabled(false);
		assertEquals(false, canvas.isEnabled());
	}

	@Test
	public void test_matchesEnabledAndDisabledProperties() {
		canvas.setEnabled(false);
		assertEquals(canvas.isEnabled(), !canvas.getDisabled());

		canvas.setDisabled(false);
		assertEquals(canvas.getDisabled(), !canvas.isEnabled());
	}

	@Test
	public void test_paintsDisabledPropertyAsDynamic() throws PaintException {
		final JsonPaintTarget target = mock(JsonPaintTarget.class);

		canvas.setDisabled(true);
		canvas.paintContent(target);
		verify(target).addAttribute("b$disabled", true);
	}
}
