package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.layout.VLayout;

public class VLayoutBuilderTest {
	@Test
	public void test_returnsNewVLayoutBuilder() {
		assertNotNull(VLayoutBuilder.buildVLayout());
	}

	@Test
	public void test_returnsNewVLayout() {
		assertNotNull(VLayoutBuilder.buildVLayout().build());
	}

	private VLayoutBuilder vLayoutBuilder;

	@Before
	public void before() {
		vLayoutBuilder = new VLayoutBuilder(mock(VLayout.class));
	}

	@Test
	public void test_meReturnsBuilder() {
		assertEquals(vLayoutBuilder, vLayoutBuilder.me());
	}
}
