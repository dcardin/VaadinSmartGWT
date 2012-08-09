package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.types.Alignment;
import org.vaadin.smartgwt.server.types.Overflow;

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
	public void test_setsMinWidth() {
		canvasBuilder.setMinWidth(100);
		verify(canvas).setMinWidth(100);
	}

	@Test
	public void test_returnsBuilderWhenSettingMinWidth() {
		assertEquals(canvasBuilder, canvasBuilder.setMinWidth(100));
	}

	@Test
	public void test_setsAutoWidth() {
		canvasBuilder.setAutoWidth();
		verify(canvas).setAutoWidth();
	}

	@Test
	public void test_returnsBuilderWhenSettingAutoWidth() {
		assertEquals(canvasBuilder, canvasBuilder.setAutoWidth());
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
	public void test_setsMinHeight() {
		canvasBuilder.setMinHeight(100);
		verify(canvas).setMinHeight(100);
	}

	@Test
	public void test_returnsBuilderWhenSettingMinHeight() {
		assertEquals(canvasBuilder, canvasBuilder.setMinHeight(100));
	}

	@Test
	public void test_setsAutoHeight() {
		canvasBuilder.setAutoHeight();
		verify(canvas).setAutoHeight();
	}

	@Test
	public void test_returnsBuilderWhenSettingAutoHeight() {
		assertEquals(canvasBuilder, canvasBuilder.setAutoHeight());
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

	@Test
	public void test_setsStyleName() {
		canvasBuilder.setStyleName("style");
		verify(canvas).setStyleName("style");
	}

	@Test
	public void test_returnsBuilderWhenSettingStyleName() {
		assertEquals(canvasBuilder, canvasBuilder.setStyleName("style"));
	}

	@Test
	public void test_setsShowEdges() {
		canvasBuilder.setShowEdges(true);
		verify(canvas).setShowEdges(true);
	}

	@Test
	public void test_returnsBuilderWhenSettingShowEdges() {
		assertEquals(canvasBuilder, canvasBuilder.setShowEdges(true));
	}

	@Test
	public void test_setsPadding() {
		canvasBuilder.setPadding(10);
		verify(canvas).setPadding(10);
	}

	@Test
	public void test_returnsBuilderWhenSettingPadding() {
		assertEquals(canvasBuilder, canvasBuilder.setPadding(10));
	}

	@Test
	public void test_setsShowResizeBar() {
		canvasBuilder.setResizeBarVisible(true);
		verify(canvas).setShowResizeBar(true);
	}

	@Test
	public void test_returnsBuilderWhenSettingShowResizeBar() {
		assertEquals(canvasBuilder, canvasBuilder.setResizeBarVisible(true));
	}

	@Test
	public void test_setsResizeBarTarget() {
		canvasBuilder.setResizeBarTarget("target");
		verify(canvas).setResizeBarTarget("target");
	}

	@Test
	public void test_returnsBuilderWhenSettingResizeBarTarget() {
		assertEquals(canvasBuilder, canvasBuilder.setResizeBarTarget("target"));
	}

	@Test
	public void test_setsOverflow() {
		canvasBuilder.setOverflow(Overflow.AUTO);
		verify(canvas).setOverflow(Overflow.AUTO);
	}

	@Test
	public void test_returnsBuilderWhenSettingOverflow() {
		assertEquals(canvasBuilder, canvasBuilder.setOverflow(Overflow.AUTO));
	}
}
