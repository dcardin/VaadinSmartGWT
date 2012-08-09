package org.vaadin.smartgwt.server.builder;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.FormItem;

public class DynamicFormBuilderTest {
	@Test
	public void test_createsDynamicFormBuilder() {
		assertNotNull(DynamicFormBuilder.buildDynamicForm());
	}

	@Test
	public void test_buildsDynamicForm() {
		assertNotNull(DynamicFormBuilder.buildDynamicForm().build());
	}

	private DynamicFormBuilder dynamicFormBuilder;
	private DynamicForm dynamicForm;

	@Before
	public void before() {
		dynamicForm = mock(DynamicForm.class);
		dynamicFormBuilder = new DynamicFormBuilder(dynamicForm);
	}

	@Test
	public void test_setsNumCols() {
		dynamicFormBuilder.setNumCols(2);
		verify(dynamicForm).setNumCols(2);
	}

	@Test
	public void test_returnsBuilderWhenSettingNumCols() {
		assertEquals(dynamicFormBuilder, dynamicFormBuilder.setNumCols(2));
	}

	@Test
	public void test_setsAutoFocus() {
		dynamicFormBuilder.setAutoFocus(true);
		verify(dynamicForm).setAutoFocus(true);
	}

	@Test
	public void test_returnsBuilderWhenSettingAutoFocus() {
		assertEquals(dynamicFormBuilder, dynamicFormBuilder.setAutoFocus(true));
	}

	@Test
	public void test_setsFields() {
		final FormItem[] fields = new FormItem[] { new FormItem() };
		dynamicFormBuilder.setFields(fields);
		verify(dynamicForm).setFields(fields);
	}

	@Test
	public void test_returnsBuilderWhenSettingFields() {
		assertEquals(dynamicFormBuilder, dynamicFormBuilder.setFields());
	}

	@Test
	public void test_meReturnsBuilder() {
		assertEquals(dynamicFormBuilder, dynamicFormBuilder.me());
	}
}
