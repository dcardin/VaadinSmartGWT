package org.vaadin.smartgwt.server.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.vaadin.ui.Component;

public class ComponentPropertyPainterTest
{
	private ComponentPropertyPainter componentPropertyPainter;

	@Before
	public void before()
	{
		componentPropertyPainter = new ComponentPropertyPainter(mock(Component.class));
	}

	@Test
	public void test_registerNewPaintableArrayProperty()
	{
		final ComponentArray<Component> paintableArray = componentPropertyPainter.addComponentArray("propertyName");
		assertTrue(componentPropertyPainter.getComponentProperties().contains(paintableArray));
	}

	@Test
	public void test_registersNewPaintableArrayPropertyWith$PrefixedName()
	{
		final ComponentArray<Component> paintableArray = componentPropertyPainter.addComponentArray("propertyName");
		assertEquals("$" + "propertyName", paintableArray.getTagName());
	}
}
