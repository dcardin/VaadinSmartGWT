package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.layout.BorderLayout;

public class BorderLayoutBuilderTest {
	private BorderLayoutBuilder borderLayoutBuilder;
	private BorderLayout borderLayout;

	@Before
	public void before() {
		borderLayout = mock(BorderLayout.class);
		borderLayoutBuilder = new BorderLayoutBuilder(borderLayout);
	}

	@Test
	public void test_createsBorderLayoutBuilder() {
		assertNotNull(BorderLayoutBuilder.buildBorderLayout());
	}

	@Test
	public void test_createsBorderLayout() {
		assertNotNull(BorderLayoutBuilder.buildBorderLayout().build());
	}

	@Test
	public void test_setsNorthMember() {
		final Canvas member = new Canvas();
		borderLayoutBuilder.setNorthMember(member);
		verify(borderLayout).setNorthMember(member);
	}

	@Test
	public void test_returnsBuilderWhenSettingNorthMember() {
		assertEquals(borderLayoutBuilder, borderLayoutBuilder.setNorthMember(new Canvas()));
	}

	@Test
	public void test_setsSouthMember() {
		final Canvas member = new Canvas();
		borderLayoutBuilder.setSouthMember(member);
		verify(borderLayout).setSouthMember(member);
	}

	@Test
	public void test_returnsBuilderWhenSettingSouthMember() {
		assertEquals(borderLayoutBuilder, borderLayoutBuilder.setSouthMember(new Canvas()));
	}

	@Test
	public void test_setsWestMember() {
		final Canvas member = new Canvas();
		borderLayoutBuilder.setWestMember(member);
		verify(borderLayout).setWestMember(member);
	}

	@Test
	public void test_returnsBuilderWhenSettingWestMember() {
		assertEquals(borderLayoutBuilder, borderLayoutBuilder.setWestMember(new Canvas()));
	}

	@Test
	public void test_setsEastMember() {
		final Canvas member = new Canvas();
		borderLayoutBuilder.setEastMember(member);
		verify(borderLayout).setEastMember(member);
	}

	@Test
	public void test_returnsBuilderWhenSettingEastMember() {
		assertEquals(borderLayoutBuilder, borderLayoutBuilder.setEastMember(new Canvas()));
	}

	@Test
	public void test_setsCenterMember() {
		final Canvas member = new Canvas();
		borderLayoutBuilder.setCenterMember(member);
		verify(borderLayout).setCenterMember(member);
	}

	@Test
	public void test_returnsBuilderWhenSettingCenterMember() {
		assertEquals(borderLayoutBuilder, borderLayoutBuilder.setCenterMember(new Canvas()));
	}

	@Test
	public void test_mePropertyReturnsBuilder() {
		assertEquals(borderLayoutBuilder, borderLayoutBuilder.me());
	}
}
