package org.vaadin.smartgwt.server.extra;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.extra.HSplitLayout.Builder;
import org.vaadin.smartgwt.server.layout.HLayout;
import org.vaadin.smartgwt.server.layout.Layout;
import org.vaadin.smartgwt.server.types.Overflow;

public class HSplitLayoutTest {
	public static class BuilderTest {
		private Builder builder;
		private HSplitLayout hSplitLayout;

		@Before
		public void before() {
			hSplitLayout = mock(HSplitLayout.class);
			builder = new HSplitLayout.Builder(hSplitLayout);
		}

		@Test
		public void test_setsProportions() {
			builder.setProportions(0.5d);
			verify(hSplitLayout).setProportions(0.5d);
		}

		@Test
		public void test_returnsBuilderWhenSettingProportions() {
			assertEquals(builder, builder.setProportions(0.5d));
		}

		@Test
		public void test_setsResizeBarVisible() {
			builder.setResizeBarVisible(true);
			verify(hSplitLayout).setResizeBarVisible(true);
		}

		@Test
		public void test_returnsBuilderWhenSettingResizeBarVisible() {
			assertEquals(builder, builder.setProportions(0.5d));
		}

		@Test
		public void test_setsLeftMember() {
			final Canvas member = new Canvas();
			builder.setLeftMember(member);
			verify(hSplitLayout).setLeftMember(member);
		}

		@Test
		public void test_returnsBuilderWhenSettingLeftMember() {
			assertEquals(builder, builder.setLeftMember(null));
		}

		@Test
		public void test_setsRightMember() {
			final Canvas member = new Canvas();
			builder.setRightMember(member);
			verify(hSplitLayout).setRightMember(member);
		}

		@Test
		public void test_returnsBuilderWhenSettingRightMember() {
			assertEquals(builder, builder.setRightMember(null));
		}

		@Test
		public void test_meReturnsBuilder() {
			assertEquals(builder, builder.me());
		}
	}

	@Test
	public void test_createsBuilder() {
		assertNotNull(HSplitLayout.buildHSplitLayout());
	}

	@Test
	public void test_createsHSplitLayout() {
		assertNotNull(HSplitLayout.buildHSplitLayout().build());
	}

	private HSplitLayout hSplitLayout;

	@Before
	public void before() {
		hSplitLayout = new HSplitLayout();
	}

	@Test
	public void test_createsLeftHolder() {
		final HLayout leftHolder = (HLayout) hSplitLayout.getMember(0);
		assertEquals("100%", leftHolder.getHeightAsString());
		assertEquals("50%", leftHolder.getWidthAsString());
		assertEquals(Overflow.AUTO, leftHolder.getOverflow());
		assertTrue(leftHolder.getShowResizeBar());
		assertEquals("next", leftHolder.getResizeBarTarget());
	}

	@Test
	public void test_createsRightHolder() {
		final HLayout rightHolder = (HLayout) hSplitLayout.getMember(1);
		assertEquals("100%", rightHolder.getHeightAsString());
		assertEquals("*", rightHolder.getWidthAsString());
		assertEquals(Overflow.AUTO, rightHolder.getOverflow());
	}

	@Test
	public void test_settingProportionsSetsLeftHolderWidth() {
		hSplitLayout.setProportions(0.3d);
		assertEquals("30%", hSplitLayout.getMember(0).getWidthAsString());
	}

	@Test
	public void test_setsResizeBarVisibility() {
		hSplitLayout.setResizeBarVisible(false);
		assertFalse(hSplitLayout.isResizeBarVisible());
	}

	@Test
	public void test_settingResizeBarVisibilitySetsLeftHolderResizeBar() {
		hSplitLayout.setResizeBarVisible(false);
		assertFalse(hSplitLayout.getMember(0).getShowResizeBar());
	}

	@Test
	public void test_setsLeftMember() {
		final Canvas member = new Canvas();
		hSplitLayout.setLeftMember(member);
		assertEquals(member, hSplitLayout.getLeftMember());
	}

	@Test
	public void test_returnsNullIfNoLeftMemberSet() {
		assertNull(hSplitLayout.getLeftMember());
	}

	@Test
	public void test_setsLeftMemberAsMemberOfLeftHolder() {
		final Canvas member = new Canvas();
		hSplitLayout.setLeftMember(member);
		assertEquals(member, ((Layout) hSplitLayout.getMember(0)).getMember(0));
	}

	@Test
	public void test_setsLeftMemberToFullSize() {
		final Canvas member = new Canvas();
		hSplitLayout.setLeftMember(member);
		assertEquals("100%", member.getWidthAsString());
		assertEquals("100%", member.getHeightAsString());
	}

	@Test
	public void test_setsRightMember() {
		final Canvas member = new Canvas();
		hSplitLayout.setRightMember(member);
		assertEquals(member, hSplitLayout.getRightMember());
	}

	@Test
	public void test_returnsNullIfNoRightMemberSet() {
		assertNull(hSplitLayout.getRightMember());
	}

	@Test
	public void test_setsRightMemberAsMemberOfRightHolder() {
		final Canvas member = new Canvas();
		hSplitLayout.setRightMember(member);
		assertEquals(member, ((Layout) hSplitLayout.getMember(1)).getMember(0));
	}

	@Test
	public void test_setsRightMemberToFullSize() {
		final Canvas member = new Canvas();
		hSplitLayout.setRightMember(member);
		assertEquals("100%", member.getWidthAsString());
		assertEquals("100%", member.getHeightAsString());
	}
}
