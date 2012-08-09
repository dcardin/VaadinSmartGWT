package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.layout.HLayout;

public class HLayoutBuilderTest {
	@Test
	public void test_createsHLayoutBuilder() {
		assertNotNull(HLayoutBuilder.buildHLayout());
	}

	@Test
	public void test_createsHLayoutInstance() {
		assertNotNull(HLayoutBuilder.buildHLayout().build());
	}

	private HLayoutBuilder hLayoutBuilder;

	@Before
	public void before() {
		hLayoutBuilder = new HLayoutBuilder(mock(HLayout.class));
	}

	@Test
	public void test_meReturnsBuilderInstance() {
		assertEquals(hLayoutBuilder, hLayoutBuilder.me());
	}
}
