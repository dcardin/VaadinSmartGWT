package org.vaadin.smartgwt.server.form.fields;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.vaadin.smartgwt.server.core.ComponentArray;
import org.vaadin.smartgwt.server.core.ComponentPropertyPainter;

import com.google.common.collect.Maps;
import com.smartgwt.client.types.FormItemType;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

public class FormItemTest {
	private FormItem formItem;
	private ComponentPropertyPainter propertyPainter;
	private ComponentArray<FormItemIcon> icons;
	private ArgumentCaptor<PropertyChangeEvent> propertyChangeEventCaptor;

	@Before
	public void before()
	{
		propertyPainter = mock(ComponentPropertyPainter.class);
		icons = mock(ComponentArray.class);
		when(propertyPainter.<FormItemIcon> addComponentArray("icons")).thenReturn(icons);

		formItem = new FormItem();
		propertyChangeEventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
	}

	@Test
	public void test_firesEventOnValueChange() {
		formItem.setValue("oldValue");

		final PropertyChangeListener listener = mock(PropertyChangeListener.class);
		formItem.addPropertyChangeListener(FormItem.PROPERTYNAME_VALUE, listener);

		formItem.setValue("newValue");
		verify(listener).propertyChange(propertyChangeEventCaptor.capture());
		assertPropertyChangeEvent(propertyChangeEventCaptor.getValue(), formItem, FormItem.PROPERTYNAME_VALUE, "oldValue", "newValue");
	}

	@Test
	public void test_firesEventOnValueVariableChange() {
		formItem.setValue("oldValue");

		final PropertyChangeListener listener = mock(PropertyChangeListener.class);
		formItem.addPropertyChangeListener(FormItem.PROPERTYNAME_VALUE, listener);

		formItem.changeVariables(formItem, Collections.<String, Object> singletonMap("value", "newValue"));
		verify(listener).propertyChange(propertyChangeEventCaptor.capture());
		assertPropertyChangeEvent(propertyChangeEventCaptor.getValue(), formItem, FormItem.PROPERTYNAME_VALUE, "oldValue", "newValue");
	}

	@Test
	public void test_setIcons() {
		final FormItemIcon icon = new FormItemIcon();
		formItem.setIcons(icon);
		assertArrayEquals(new FormItemIcon[] { icon }, formItem.getIcons());
	}

	@Test
	public void test_paintsIconsOnPaint() throws PaintException {
		final FormItemIcon icon = mock(FormItemIcon.class);
		final PaintTarget target = mock(JsonPaintTarget.class);
		formItem.setIcons(icon);
		formItem.paint(target);
		verify(icon).paint(target);
	}

	@Test
	public void test_paintsBooleanValue() throws Exception {
		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);

		formItem.setType(FormItemType.BOOLEAN.getValue());
		formItem.setValue(Boolean.TRUE);
		formItem.paintContent(paintTarget);
		verify(paintTarget, atLeastOnce()).addAttribute("value", "b" + Boolean.TRUE.toString());
	}

	@Test
	public void test_updatesBooleanValueVariable() {
		final HashMap<String, Object> variables = Maps.<String, Object> newHashMap();

		variables.put("value", "true");
		formItem.setType(FormItemType.BOOLEAN.getValue());
		formItem.changeVariables(null, variables);
		assertEquals(Boolean.TRUE, formItem.getValue());
	}

	private static void assertPropertyChangeEvent(PropertyChangeEvent event, Object source, String propertyName, Object oldValue, Object newValue) {
		assertEquals(source, event.getSource());
		assertEquals(propertyName, event.getPropertyName());
		assertEquals(oldValue, event.getOldValue());
		assertEquals(newValue, event.getNewValue());
	}
}
