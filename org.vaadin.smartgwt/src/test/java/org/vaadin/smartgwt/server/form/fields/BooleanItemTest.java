package org.vaadin.smartgwt.server.form.fields;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.smartgwt.client.types.FormItemType;

public class BooleanItemTest {
	private BooleanItem booleanItem;
	private String name;
	private String title;

	@Before
	public void before() {
		booleanItem = new BooleanItem(name = "name", title = "title");
	}

	@Test
	public void test_initialisesNameProperty() {
		assertEquals(name, booleanItem.getName());
	}

	@Test
	public void test_initialisesTitleProperty() {
		assertEquals(title, booleanItem.getTitle());
	}

	@Test
	public void test_initialisesFormItemTypeToBoolean() {
		assertEquals(FormItemType.BOOLEAN.getValue(), booleanItem.getType());
	}

	@Test
	public void test_getValueAsBoolean() {
		booleanItem.setValue(Boolean.TRUE);
		assertEquals(Boolean.TRUE, booleanItem.getValueAsBoolean());
	}
}
