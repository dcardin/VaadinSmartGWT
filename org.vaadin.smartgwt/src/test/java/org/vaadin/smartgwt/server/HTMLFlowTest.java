package org.vaadin.smartgwt.server;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class HTMLFlowTest {
	private HTMLFlow htmlFlow;

	@Before
	public void before() {
		htmlFlow = new HTMLFlow();
	}

	@Test
	public void test_contentsResourceProperty() {
		final Resource resource = mock(Resource.class);

		htmlFlow.setContentsResource(resource);
		assertEquals(resource, htmlFlow.getContentsResource());
	}

	@Test
	public void test_ignoresContentsResourceAttributeWhenUnset() throws PaintException {
		final JsonPaintTarget target = mock(JsonPaintTarget.class);

		htmlFlow.paintContent(target);
		verify(target, never()).addAttribute(eq("*contentsResource"), any(Resource.class));
	}

	@Test
	public void test_addsContentsResourceAttributeWhenSet() throws PaintException {
		final Resource resource = mock(Resource.class);
		final JsonPaintTarget target = mock(JsonPaintTarget.class);
		
		htmlFlow.setContentsResource(resource);
		htmlFlow.paintContent(target);
		verify(target).addAttribute("*contentsResource", resource);
	}
}
