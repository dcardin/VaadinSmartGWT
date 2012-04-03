package org.vaadin.smartgwt.server.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;

public class PaintableArrayTest
{
	private final String tagName = "propertyName";
	private final Paintable[] paintables = new Paintable[0];
	private PaintableArray<Paintable> paintableArray;

	@Before
	public void before()
	{
		paintableArray = new PaintableArray<Paintable>(tagName, paintables);
	}

	@Test
	public void test_injectsTagName()
	{
		assertEquals(tagName, paintableArray.getTagName());
	}

	@Test
	public void test_injectsPaintableArrayReference()
	{
		assertSame(paintables, paintableArray.get());
	}

	@Test
	public void test_setPaintableArrayReference()
	{
		final Paintable[] paintables = new Paintable[0];
		paintableArray.set(paintables);
		assertSame(paintables, paintableArray.get());
	}

	@Test
	public void test_paintsNullPaintablesArray() throws PaintException
	{
		final PaintTarget target = mock(PaintTarget.class);
		paintableArray.paintContent(target);

		final InOrder inOrder = inOrder(target);
		inOrder.verify(target).startTag(tagName);
		inOrder.verify(target).addAttribute("type", "Array");
		inOrder.verify(target).endTag(tagName);
		verifyNoMoreInteractions(target);
	}

	@Test
	public void test_paintsPaintablesArray() throws PaintException
	{
		final Paintable paintable = mock(Paintable.class);
		final PaintTarget target = mock(PaintTarget.class);

		paintableArray.set(new Paintable[] { paintable });
		paintableArray.paintContent(target);

		final InOrder inOrder = inOrder(target, paintable);
		inOrder.verify(target).startTag(tagName);
		inOrder.verify(target).addAttribute("type", "Array");
		inOrder.verify(paintable).paint(target);
		inOrder.verify(target).endTag(tagName);
		verifyNoMoreInteractions(target, paintable);
	}
}
