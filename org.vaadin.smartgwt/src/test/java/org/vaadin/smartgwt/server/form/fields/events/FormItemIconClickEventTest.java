package org.vaadin.smartgwt.server.form.fields.events;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.FormItem;
import org.vaadin.smartgwt.server.form.fields.FormItemIcon;

public class FormItemIconClickEventTest {
	@Test
	public void test_returnsSameEventTypeOnMultipleCalls() {
		assertSame(FormItemIconClickEvent.getType(), FormItemIconClickEvent.getType());
	}

	private FormItemIconClickEvent event;
	private DynamicForm form;
	private FormItem item;
	private FormItemIcon icon;

	@Before
	public void before() {
		event = new FormItemIconClickEvent(form = mock(DynamicForm.class), item = mock(FormItem.class), icon = mock(FormItemIcon.class));
	}

	@Test
	public void test_associatedTypePropertyReturnsStaticType() {
		assertSame(FormItemIconClickEvent.getType(), event.getAssociatedType());
	}

	@Test
	public void test_formProperty() {
		assertEquals(form, event.getForm());
	}

	@Test
	public void test_itemProperty() {
		assertEquals(item, event.getItem());
	}

	@Test
	public void test_iconProperty() {
		assertEquals(icon, event.getIcon());
	}

	@Test
	public void test_dispatchEventToHandler() {
		final FormItemClickHandler handler = mock(FormItemClickHandler.class);
		event.dispatch(handler);
		verify(handler).onFormItemClick(event);
	}
}
