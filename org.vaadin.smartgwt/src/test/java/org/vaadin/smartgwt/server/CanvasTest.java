package org.vaadin.smartgwt.server;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.events.ClickEvent;
import org.vaadin.smartgwt.server.events.ClickHandler;

import com.google.common.collect.Maps;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
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

	@Test
	public void test_paintsClickHandlerRegistrationState() throws PaintException {
		final PaintTarget target = mock(JsonPaintTarget.class);
		final ClickHandler handler = mock(ClickHandler.class);

		canvas.addClickHandler(handler);
		canvas.paintContent(target);
		verify(target).addAttribute("*hasClickHandlers", true);
	}

	@Test
	public void test_doNotPaintClickHandlerRegistrationStateWhenNoneRegistered() throws PaintException {
		final PaintTarget target = mock(JsonPaintTarget.class);

		canvas.paintContent(target);
		verify(target, never()).addAttribute("*hasClickHandlers", true);
	}

	@Test
	public void test_notifiesClickHandlersOnClickEventVariableChange() {
		final ClickHandler handler = mock(ClickHandler.class);
		final HashMap<String, Object> variables = Maps.newHashMap();

		variables.put("clickEvent", true);
		canvas.addClickHandler(handler);

		canvas.changeVariables(null, variables);
		verify(handler).onClick(isA(ClickEvent.class));
	}

	@Test
	public void test_unregistersClickHandlerWithHandlerRegistration() {
		final ClickHandler handler = mock(ClickHandler.class);
		final HandlerRegistration handlerRegistration;
		final HashMap<String, Object> variables = Maps.newHashMap();

		variables.put("clickEvent", true);
		handlerRegistration = canvas.addClickHandler(handler);
		handlerRegistration.removeHandler();

		canvas.changeVariables(null, variables);
		verify(handler, never()).onClick(isA(ClickEvent.class));
	}
}
