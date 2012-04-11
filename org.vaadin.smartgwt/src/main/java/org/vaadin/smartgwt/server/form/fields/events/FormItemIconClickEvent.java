/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.vaadin.smartgwt.server.form.fields.events;

import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.FormItem;
import org.vaadin.smartgwt.server.form.fields.FormItemIcon;

import com.google.web.bindery.event.shared.Event;

public class FormItemIconClickEvent extends Event<FormItemClickHandler>
{
	private static Event.Type<FormItemClickHandler> TYPE;
	private final DynamicForm form;
	private final FormItem item;
	private final FormItemIcon icon;

	/**
	 * Gets the type associated with this event.
	 * 
	 * @return returns the handler type
	 */
	public static Event.Type<FormItemClickHandler> getType()
	{
		return TYPE == null ? TYPE = new Event.Type<FormItemClickHandler>() : TYPE;
	}

	public FormItemIconClickEvent(DynamicForm form, FormItem item, FormItemIcon icon)
	{
		this.form = form;
		this.item = item;
		this.icon = icon;
	}

	@Override
	public Event.Type<FormItemClickHandler> getAssociatedType()
	{
		return getType();
	}

	/**
	 * The Dynamic Form to which this icon's item belongs.
	 * 
	 * @return The Dynamic Form to which this icon's item belongs.
	 */
	public DynamicForm getForm()
	{
		return form;
	}

	/**
	 * The Form Item containing this icon
	 * 
	 * @return The Form Item containing this icon
	 */
	public FormItem getItem()
	{
		return item;
	}

	/**
	 * A pointer to the form item icon clicked
	 * 
	 * @return A pointer to the form item icon clicked
	 */
	public FormItemIcon getIcon()
	{
		return icon;
	}

	@Override
	protected void dispatch(FormItemClickHandler handler)
	{
		handler.onFormItemClick(this);
	}
}
