package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.BaseWidget;

public class BaseWidgetBuilderTest {
	private static class TestBaseWidgetBuilder extends BaseWidgetBuilder<BaseWidget, TestBaseWidgetBuilder> {
		public TestBaseWidgetBuilder(BaseWidget instance) {
			super(instance);
		}

		@Override
		protected TestBaseWidgetBuilder me() {
			return this;
		}
	}

	private TestBaseWidgetBuilder baseWidgetBuilder;
	private BaseWidget baseWidget;

	@Before
	public void before() {
		baseWidget = mock(BaseWidget.class);
		baseWidgetBuilder = new TestBaseWidgetBuilder(baseWidget);
	}

	@Test
	public void test_instanceProperty() {
		assertEquals(baseWidget, baseWidgetBuilder.instance());
	}

	@Test
	public void test_returnsBuiltInstance() {
		assertEquals(baseWidget, baseWidgetBuilder.build());
	}
}
