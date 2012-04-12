package org.vaadin.smartgwt.server.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

public class ComponentArrayTest {
	private final String tagName = "propertyName";
	private ComponentArray<Component> componentArray;

	@Before
	public void before() {
		componentArray = new ComponentArray<Component>(mock(Component.class), tagName);
	}

	@Test
	public void test_injectsTagName() {
		assertEquals(tagName, componentArray.getTagName());
	}

	@Test
	public void test_setPaintableArrayReference() {
		final Component[] components = new Component[0];
		componentArray.set(components);
		assertSame(components, componentArray.get());
	}

	@Test
	public void test_paintsNullPaintablesArray() throws PaintException {
		final PaintTarget target = mock(PaintTarget.class);
		componentArray.paintContent(target);

		final InOrder inOrder = inOrder(target);
		inOrder.verify(target).startTag(tagName);
		inOrder.verify(target).addAttribute("type", "Array");
		inOrder.verify(target).endTag(tagName);
	}

	@Test
	public void test_paintsPaintablesArray() throws PaintException {
		final Component component = mock(Component.class);
		final PaintTarget target = mock(PaintTarget.class);

		componentArray.set(new Component[] { component });
		componentArray.paintContent(target);

		final InOrder inOrder = inOrder(target, component);
		inOrder.verify(target).startTag(tagName);
		inOrder.verify(target).addAttribute("type", "Array");
		inOrder.verify(component).paint(target);
		inOrder.verify(target).endTag(tagName);
	}
}
