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
		final PaintableArray<Component> paintableArray = componentPropertyPainter.addPaintableArray("propertyName");
		assertTrue(componentPropertyPainter.getPaintableProperties().contains(paintableArray));
	}

	@Test
	public void test_registersNewPaintableArrayPropertyWith$PrefixedName()
	{
		final PaintableArray<Component> paintableArray = componentPropertyPainter.addPaintableArray("propertyName");
		assertEquals("$" + "propertyName", paintableArray.getTagName());
	}
}
