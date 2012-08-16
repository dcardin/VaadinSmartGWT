package org.vaadin.smartgwt.server.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Iterators;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;
import com.vaadin.ui.Component;

public class ComponentListTest {
	private ComponentList<Component> componentList;

	@Before
	public void before() {
		componentList = new ComponentList<Component>(mock(Component.class), "components");
	}

	@Test
	public void test_paintsRemovalInstruction() throws PaintException {
		final Component component = mock(Component.class);
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);

		// adds component and paint to clear instructions.
		componentList.add(component);
		componentList.paintContent(mock(JsonPaintTarget.class));

		componentList.remove(component);
		componentList.paintContent(paintTarget);

		verify(paintTarget).startTag("remove");
		verify(paintTarget).addAttribute("element", component);
	}

	@Test
	public void test_removesComponentThatWasRemovedClientSide() {
		final Component component = mock(Component.class);

		componentList.add(component);
		componentList.clientRemoved(component);

		assertFalse(Iterators.contains(componentList.iterator(), component));
	}

	@Test
	public void test_noPaintWhenClientRemoved() throws PaintException {
		final Component component = mock(Component.class);
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);

		// adds component and paint to clear instructions.
		componentList.add(component);
		componentList.paintContent(mock(JsonPaintTarget.class));

		componentList.clientRemoved(component);
		componentList.paintContent(paintTarget);

		verify(paintTarget, never()).startTag("remove");
	}
}
