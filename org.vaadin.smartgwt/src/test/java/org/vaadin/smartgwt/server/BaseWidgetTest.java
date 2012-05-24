package org.vaadin.smartgwt.server;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class BaseWidgetTest {
	private BaseWidget baseWidget;

	@Before
	public void before() {
		baseWidget = new BaseWidget() {
		};
	}

	@Test
	public void test_paintsDynamicBooleanAttribute() throws PaintException {
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);

		baseWidget.setAttribute("b$attribute", true, true);

		baseWidget.paintContent(paintTarget);
		verify(paintTarget).addAttribute("b$attribute", true);
	}
}
