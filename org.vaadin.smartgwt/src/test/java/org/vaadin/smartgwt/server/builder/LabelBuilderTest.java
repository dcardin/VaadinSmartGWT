package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.Label;

public class LabelBuilderTest {
	@Test
	public void test_createsNewLabelBuilder() {
		assertNotNull(LabelBuilder.buildLabel("contents"));
	}

	@Test
	public void test_createsNewLabel() {
		assertNotNull(LabelBuilder.buildLabel("contents").build());
	}

	@Test
	public void test_setsLabelContent() {
		assertEquals("contents", LabelBuilder.buildLabel("contents").build().getContents());
	}

	private LabelBuilder labelBuilder;

	@Before
	public void before() {
		labelBuilder = new LabelBuilder(mock(Label.class));
	}

	@Test
	public void test_meReturnsBuilderInstance() {
		assertEquals(labelBuilder, labelBuilder.me());
	}
}
