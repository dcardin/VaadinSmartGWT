package org.vaadin.smartgwt.server.form.fields;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

public class FormItemTest
{
	private FormItem formItem;
	private ArgumentCaptor<PropertyChangeEvent> propertyChangeEventCaptor;

	@Before
	public void before()
	{
		formItem = new FormItem();
		propertyChangeEventCaptor = ArgumentCaptor.forClass(PropertyChangeEvent.class);
	}

	@Test
	public void test_firesEventOnValueChange()
	{
		formItem.setValue("oldValue");

		final PropertyChangeListener listener = mock(PropertyChangeListener.class);
		formItem.addPropertyChangeListener(FormItem.PROPERTYNAME_VALUE, listener);

		formItem.setValue("newValue");
		verify(listener).propertyChange(propertyChangeEventCaptor.capture());
		assertPropertyChangeEvent(propertyChangeEventCaptor.getValue(), formItem, FormItem.PROPERTYNAME_VALUE, "oldValue", "newValue");
	}

	@Test
	public void test_firesEventOnValueVariableChange()
	{
		formItem.setValue("oldValue");

		final PropertyChangeListener listener = mock(PropertyChangeListener.class);
		formItem.addPropertyChangeListener(FormItem.PROPERTYNAME_VALUE, listener);

		formItem.changeVariables(formItem, Collections.<String, Object> singletonMap("value", "newValue"));
		verify(listener).propertyChange(propertyChangeEventCaptor.capture());
		assertPropertyChangeEvent(propertyChangeEventCaptor.getValue(), formItem, FormItem.PROPERTYNAME_VALUE, "oldValue", "newValue");
	}

	private static void assertPropertyChangeEvent(PropertyChangeEvent event, Object source, String propertyName, Object oldValue, Object newValue)
	{
		assertEquals(source, event.getSource());
		assertEquals(propertyName, event.getPropertyName());
		assertEquals(oldValue, event.getOldValue());
		assertEquals(newValue, event.getNewValue());
	}
}
