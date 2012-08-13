package org.vaadin.smartgwt.server.extra;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.layout.Layout;
import org.vaadin.smartgwt.server.layout.VLayout;

public class VSplitLayoutTest {
	public static class BuilderTest {
		private VSplitLayout.Builder builder;
		private VSplitLayout vSplitLayout;

		@Before
		public void before() {
			vSplitLayout = mock(VSplitLayout.class);
			builder = new VSplitLayout.Builder(vSplitLayout);
		}

		@Test
		public void test_setsProportions() {
			builder.setProportions(0.5d);
			verify(vSplitLayout).setProportions(0.5d);
		}

		@Test
		public void test_returnsBuilderWhenSettingProportions() {
			assertEquals(builder, builder.setProportions(0.5d));
		}

		@Test
		public void test_returnsBuilderWhenSettingResizeBarVisible() {
			assertEquals(builder, builder.setProportions(0.5d));
		}

		@Test
		public void test_setsTopMember() {
			final Canvas member = new Canvas();
			builder.setTopMember(member);
			verify(vSplitLayout).setTopMember(member);
		}

		@Test
		public void test_returnsBuilderWhenSettingTopMember() {
			assertEquals(builder, builder.setTopMember(null));
		}

		@Test
		public void test_setsBottomMember() {
			final Canvas member = new Canvas();
			builder.setBottomMember(member);
			verify(vSplitLayout).setBottomMember(member);
		}

		@Test
		public void test_returnsBuilderWhenSettingBottomMember() {
			assertEquals(builder, builder.setBottomMember(null));
		}

		@Test
		public void test_meReturnsBuilder() {
			assertEquals(builder, builder.me());
		}
	}

	@Test
	public void test_createsBuilder() {
		assertNotNull(VSplitLayout.buildVSplitLayout());
	}

	@Test
	public void test_createsVSplitLayout() {
		assertNotNull(VSplitLayout.buildVSplitLayout().build());
	}

	private VSplitLayout vSplitLayout;

	@Before
	public void before() {
		vSplitLayout = new VSplitLayout();
	}

	@Test
	public void test_createsTopHolder() {
		final VLayout topHolder = (VLayout) vSplitLayout.getMember(0);
		assertEquals("50%", topHolder.getHeightAsString());
		assertEquals(Integer.valueOf(1), topHolder.getAttributeAsInt("width"));
	}

	@Test
	public void test_createsBottomHolder() {
		final VLayout bottomHolder = (VLayout) vSplitLayout.getMember(1);
		assertEquals("*", bottomHolder.getHeightAsString());
		assertEquals(Integer.valueOf(1), bottomHolder.getAttributeAsInt("width"));
	}

	@Test
	public void test_settingProportionsSetsTopHolderHeight() {
		vSplitLayout.setProportions(0.3d);
		assertEquals("30%", vSplitLayout.getMember(0).getHeightAsString());
	}

	@Test
	public void test_setsTopMember() {
		final Canvas member = new Canvas();
		vSplitLayout.setTopMember(member);
		assertEquals(member, vSplitLayout.getTopMember());
	}

	@Test
	public void test_returnsNullIfNoTopMemberSet() {
		assertNull(vSplitLayout.getTopMember());
	}

	@Test
	public void test_setsTopMemberAsMemberOfTopHolder() {
		final Canvas member = new Canvas();
		vSplitLayout.setTopMember(member);
		assertEquals(member, ((Layout) vSplitLayout.getMember(0)).getMember(0));
	}

	@Test
	public void test_setsTopMemberToFullSize()
	{
		final Canvas member = new Canvas();
		vSplitLayout.setTopMember(member);
		assertEquals("100%", member.getHeightAsString());
	}

	@Test
	public void test_setsBottomMember() {
		final Canvas member = new Canvas();
		vSplitLayout.setBottomMember(member);
		assertEquals(member, vSplitLayout.getBottomMember());
	}

	@Test
	public void test_returnsNullIfNoBottomMemberSet() {
		assertNull(vSplitLayout.getBottomMember());
	}

	@Test
	public void test_setsBottomMemberAsMemberOfBottomHolder() {
		final Canvas member = new Canvas();
		vSplitLayout.setBottomMember(member);
		assertEquals(member, ((Layout) vSplitLayout.getMember(1)).getMember(0));
	}

	@Test
	public void test_setsBottomMemberToFullSize() {
		final Canvas member = new Canvas();
		vSplitLayout.setBottomMember(member);
		assertEquals("100%", member.getHeightAsString());
	}
}
