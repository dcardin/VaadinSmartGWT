/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * is published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.vaadin.smartgwt.server.form.fields;

import com.vaadin.ui.ClientWidget;


/**
 * Convenience class for setting pickers for form fields. This is a specialized subclass of FormItemIcon that sets up the correct picker dimensions and spacing.
 * <p>
 * A set of standard pickers are provided as a part of the library.
 * 
 * @see FormItem#setIcons(FormItemIcon...)
 */
@ClientWidget(org.vaadin.smartgwt.client.ui.form.fields.VPickerIcon.class)
public class PickerIcon extends FormItemIcon
{
	public static Picker CLEAR = new Picker("[SKIN]/pickers/clear_picker.$IMG_TYPE");
	public static Picker COMBO_BOX = new Picker("[SKIN]/pickers/comboBoxPicker.$IMG_TYPE");
	public static Picker DATE = new Picker("[SKIN]/pickers/date_picker.$IMG_TYPE");
	public static Picker REFRESH = new Picker("[SKIN]/pickers/refresh_picker.$IMG_TYPE");
	public static Picker SEARCH = new Picker("[SKIN]/pickers/search_picker.$IMG_TYPE");

	/**
	 * Create a new HeaderControl with the specific icon.
	 * 
	 * @param icon
	 *            the icon
	 */
	public PickerIcon(Picker icon)
	{
		setSrc(icon.getUrl());
		setWidth((Integer) 18);
		setHeight((Integer) 22);
		setAttribute("hspace", 0);
	}

	public static class Picker
	{
		private final String url;

		public Picker(String url)
		{
			this.url = url;
		}

		public String getUrl()
		{
			return url;
		}
	}
}
