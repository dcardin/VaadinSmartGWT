package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.types.Alignment;

public class CanvasBuilderTest {
	public static class TestCanvasBuilder extends CanvasBuilder<Canvas, TestCanvasBuilder> {
		public TestCanvasBuilder(Canvas instance) {
			super(instance);
		}

		@Override
		protected TestCanvasBuilder me() {
			return this;
		}
	}

	private TestCanvasBuilder canvasBuilder;
	private Canvas canvas;

	@Before
	public void before() {
		canvas = mock(Canvas.class);
		canvasBuilder = new TestCanvasBuilder(canvas);
	}

	@Test
	public void test_setsIntWidth() {
		canvasBuilder.setWidth(100);
		verify(canvas).setWidth(100);
	}

	@Test
	public void test_returnsBuilderWhenSettingIntWidth() {
		assertEquals(canvasBuilder, canvasBuilder.setWidth(100));
	}

	@Test
	public void test_setsStringWidth() {
		canvasBuilder.setWidth("100");
		verify(canvas).setWidth("100");
	}

	@Test
	public void test_returnsBuilderWhenSettingStringWidth() {
		assertEquals(canvasBuilder, canvasBuilder.setWidth("100"));
	}

	@Test
	public void test_setsIntHeight() {
		canvasBuilder.setHeight(100);
		verify(canvas).setHeight(100);
	}

	@Test
	public void test_returnsBuilderWhenSettingIntHeight() {
		assertEquals(canvasBuilder, canvasBuilder.setHeight(100));
	}

	@Test
	public void test_setsStringHeight() {
		canvasBuilder.setHeight("100");
		verify(canvas).setHeight("100");
	}

	@Test
	public void test_returnsBuilderWhenSettingStringHeight() {
		assertEquals(canvasBuilder, canvasBuilder.setHeight("100"));
	}

	@Test
	public void test_setsBackgroundColor() {
		canvasBuilder.setBackgroundColor("red");
		verify(canvas).setBackgroundColor("red");
	}

	@Test
	public void test_returnsBuilderWhenSettingBackgroundColor() {
		assertEquals(canvasBuilder, canvasBuilder.setBackgroundColor("red"));
	}

	@Test
	public void test_setsAlign() {
		canvasBuilder.setAlign(Alignment.CENTER);
		verify(canvas).setAlign(Alignment.CENTER);
	}

	@Test
	public void test_returnsBuilderWhenSettingAlign() {
		assertEquals(canvasBuilder, canvasBuilder.setAlign(Alignment.CENTER));
	}

	@Test
	public void test_setsLayoutAlign() {
		canvasBuilder.setLayoutAlign(Alignment.CENTER);
		verify(canvas).setLayoutAlign(Alignment.CENTER);
	}

	@Test
	public void test_returnsBuilderWhenSettingLayoutAlign() {
		assertEquals(canvasBuilder, canvasBuilder.setLayoutAlign(Alignment.CENTER));
	}
}
