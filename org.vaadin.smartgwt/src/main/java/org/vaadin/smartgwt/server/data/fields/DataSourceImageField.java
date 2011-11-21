/*
 * SmartGWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.vaadin.smartgwt.server.data.fields;

import org.vaadin.smartgwt.client.data.fields.VDataSourceField;
import org.vaadin.smartgwt.server.data.DataSourceField;
import org.vaadin.smartgwt.server.types.FieldType;

import com.vaadin.ui.ClientWidget;


@ClientWidget(value=VDataSourceField.class)
public class DataSourceImageField extends DataSourceField {

    public DataSourceImageField() {
        setType(FieldType.IMAGE);
    }

    public DataSourceImageField(String name) {
        super(name, FieldType.IMAGE);
    }

    public DataSourceImageField(String name, String title) {
        super(name, FieldType.IMAGE, title);
    }

    public DataSourceImageField(String name, String title, int length) {
        super(name, FieldType.IMAGE, title, length);
    }

    public DataSourceImageField(String name, String title, int length, boolean required) {
        super(name, FieldType.IMAGE, title, length, required);
    }

    public void setImageHeight(String imageHeight) {
        setAttribute("imageHeight", imageHeight);
    }

    public void setImageWidth(String imageWidth) {
        setAttribute("imageWidth", imageWidth);
    }

    public void setWidth(Integer length) {
        setAttribute("width", length);
    }

    /**
     * Set the imageURLPrefix.
     *
     * @param imageURLPrefix the imageURLPrefix
     */
    public void setImageURLPrefix(String imageURLPrefix) {
        setAttribute("imageURLPrefix", imageURLPrefix);
    }

    /**
     * Return the imageURLPrefix.
     *
     * @return the imageURLPrefix
     */
    public String getImageURLPrefix() {
        return getAttribute("imageURLPrefix");
    }
}
