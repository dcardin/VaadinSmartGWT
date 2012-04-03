package org.vaadin.smartgwt.server.form.fields;

import static org.junit.Assert.*;

import org.junit.Test;
import org.vaadin.smartgwt.server.form.fields.PickerIcon.Picker;

public class PickerIconTest
{
	@Test
	public void test_configuresSrcURLFromPicker()
	{
		final Picker picker = new Picker("url");
		final PickerIcon icon = new PickerIcon(picker);
		assertEquals(picker.getUrl(), icon.getSrc());
	}

	@Test
	public void test_configuresStandardWidth()
	{
		final PickerIcon icon = new PickerIcon(new Picker(null));
		assertEquals((Integer) 18, icon.getAttributeAsInt("width"));
	}

	@Test
	public void test_configuresStandardHeight()
	{
		final PickerIcon icon = new PickerIcon(new Picker(null));
		assertEquals((Integer) 22, icon.getAttributeAsInt("height"));
	}

	@Test
	public void test_configuresStandardHSpace()
	{
		final PickerIcon icon = new PickerIcon(new Picker(null));
		assertEquals((Integer) 0, icon.getAttributeAsInt("hspace"));
	}
}
