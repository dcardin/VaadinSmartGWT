package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.layout.Layout;

public class LayoutBuilderTest {
	private static class TestLayoutBuilder extends LayoutBuilder<Layout, TestLayoutBuilder> {
		public TestLayoutBuilder(Layout instance) {
			super(instance);
		}

		@Override
		protected TestLayoutBuilder me() {
			return this;
		}
	}

	private TestLayoutBuilder layoutBuilder;
	private Layout layout;

	@Before
	public void before() {
		layout = mock(Layout.class);
		layoutBuilder = new TestLayoutBuilder(layout);
	}

	@Test
	public void test_setsMembers() {
		final Canvas[] members = new Canvas[] { new Canvas() };
		layoutBuilder.setMembers(members);
		verify(layout).setMembers(members);
	}

	@Test
	public void test_returnsBuilderWhenSettingMembers() {
		assertEquals(layoutBuilder, layoutBuilder.setMembers());
	}

	@Test
	public void test_setsMembersMargin() {
		layoutBuilder.setMembersMargin(5);
		verify(layout).setMembersMargin(5);
	}

	@Test
	public void test_returnsBuilderWhenSettingMembersMargin() {
		assertEquals(layoutBuilder, layoutBuilder.setMembersMargin(5));
	}
}
