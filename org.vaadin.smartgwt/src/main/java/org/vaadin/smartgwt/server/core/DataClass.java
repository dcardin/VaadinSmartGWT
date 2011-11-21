package org.vaadin.smartgwt.server.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.vaadin.smartgwt.server.BaseWidget;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

/*
 * SmartGWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

public class DataClass extends BaseWidget { //extends JsObject {

    public DataClass() {
//        super(JSOHelper.createObject());FormItem
    }

//    public DataClass(JavaScriptObject jsObj) {
//        super(jsObj);
//    }

//    public JavaScriptObject getJsObj() {
//        return jsObj;
//    }

//    public void setAttribute(String property, String value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public String getAttribute(String property) {
//        return JSOHelper.getAttribute(jsObj, property);
//    }
//
//    public String getAttributeAsString(String property) {
//        return JSOHelper.getAttribute(jsObj, property);
//    }
//
//    public void setAttribute(String property, int value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, double value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//    
//    public void setAttribute(String property, long value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public Integer getAttributeAsInt(String property) {
//        return JSOHelper.getAttributeAsInt(jsObj, property);
//    }
//
//    public void setAttribute(String property, boolean value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public Boolean getAttributeAsBoolean(String property) {
//        return JSOHelper.getAttributeAsBoolean(jsObj, property);
//    }
//
//    public Double getAttributeAsDouble(String property) {
//        return JSOHelper.getAttributeAsDouble(jsObj, property);
//    }
//    
//    public Long getAttributeAsLong(String property) {
//        Double dVal = this.getAttributeAsDouble(property);
//        return dVal == null ? null : dVal.longValue();
//    }
//    
//    public double[] getAttributeAsDoubleArray(String property) {
//        return JSOHelper.getAttributeAsDoubleArray(jsObj, property);
//    }
//
//    public void setAttribute(String property, int[] value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//    public void setAttribute(String property, Integer[] value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, DataClass[] value) {
//        JSOHelper.setAttribute(jsObj, property, JSOHelper.convertToJavaScriptArray(value));
//    }
//
//    public void setAttribute(String property, BaseClass[] value) {
//        JSOHelper.setAttribute(jsObj, property, JSOHelper.convertToJavaScriptArray(value));
//    }
//
//    public void setAttribute(String property, BaseWidget[] value) {
//        JSOHelper.setAttribute(jsObj, property, JSOHelper.convertToJavaScriptArray(value));
//    }
//
//
//    public int[] getAttributeAsIntArray(String property) {
//        return JSOHelper.getAttributeAsIntArray(jsObj, property);
//    }
//
//    public void setAttribute(String property, String[] value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public String[] getAttributeAsStringArray(String property) {
//        return JSOHelper.getAttributeAsStringArray(jsObj, property);
//    }
//
//    public void setAttribute(String property, DataClass value) {
//        JSOHelper.setAttribute(jsObj, property, value == null ? null : value.getJsObj());
//    }
//
//    public void setAttribute(String property, BaseClass value) {
//        JSOHelper.setAttribute(jsObj, property, value == null ? null : value.getOrCreateJsObj());
//    }
//
//
//    public void setAttribute(String property, JavaScriptObject value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, Date value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//    
//    public void setAttribute(String property, double[] value) {
//    	JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, Boolean value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, Map value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, ValueEnum[] value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, ValueEnum value) {
//        JSOHelper.setAttribute(jsObj, property, value == null ? null : value.getValue());
//    }
//    
//    /**
//     * Set the attribute value as and Object. Note that this method converts the Java primitive Object types, Dates and Maps to the underyling
//     * JavaScriptObject value. All other object types are set as Object type attributes and users are expected to call {@link #getAttributeAsObject(String)}
//     * in order to retrieve them.
//     *
//     * @param property the attribute name
//     * @param value the attribute value.
//     */
//    public void setAttribute(String property, Object value) {
//        if (value instanceof String || value == null) {
//            setAttribute(property, (String) value);
//        } else if (value instanceof Integer) {
//            setAttribute(property, ((Integer) value).intValue());
//        } else if (value instanceof Float) {
//            setAttribute(property, ((Float) value).floatValue());
//        } else if (value instanceof Double) {
//            setAttribute(property, ((Double) value).doubleValue());
//        } else if (value instanceof Long) {
//            setAttribute(property, ((Long) value).longValue());
//        } else if (value instanceof Boolean) {
//            setAttribute(property, ((Boolean) value).booleanValue());
//        } else if (value instanceof Date) {
//            setAttribute(property, (Date) value);
//        } else if (value instanceof JavaScriptObject) {
//            setAttribute(property, ((JavaScriptObject) value));
//        } else if (value instanceof Map) {
//            setAttribute(property, JSOHelper.convertMapToJavascriptObject((Map) value));
//        } else  {
//            JSOHelper.setAttribute(jsObj, property, value);
//        }
//    }
//
//    public void setAttribute(String property, Double value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, Integer value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//    public void setAttribute(String property, Float value) {
//        JSOHelper.setAttribute(jsObj, property, value);
//    }
//
//
//    public Float getAttributeAsFloat(String property) {
//        return JSOHelper.getAttributeAsFloat(jsObj, property);
//    }
//
//    public Date getAttributeAsDate(String property) {
//        return JSOHelper.getAttributeAsDate(jsObj, property);
//    }
//
//    public Object getAttributeAsObject(String property) {
//        return JSOHelper.getAttributeAsObject(jsObj, property);
//    }
//
//    public Map getAttributeAsMap(String property) {
//        return JSOHelper.getAttributeAsMap(jsObj, property);
//    }
//
//    /**
//     * Get the attribute value as a Record.
//     *
//     * @param property the property name
//     * @return the record value
//     */
//    public Record getAttributeAsRecord(String property) {
//        return Record.getOrCreateRef(getAttributeAsJavaScriptObject(property));
//    }
//
//    public JavaScriptObject getAttributeAsJavaScriptObject(String property) {
//        return JSOHelper.getAttributeAsJavaScriptObject(jsObj, property);
//    }
//    
//    public String[] getAttributes() {
//        return JSOHelper.getProperties(jsObj);
//    }

    //event handling code
//    private HandlerManager manager = null;
//
//    public void fireEvent(GwtEvent<?> event) {
//        if (manager != null) {
//            manager.fireEvent(event);
//        }
//    }

//    protected final <H extends EventHandler> HandlerRegistration doAddHandler(
//           final H handler, GwtEvent.Type<H> type) {
//        return ensureHandlers().addHandler(type, handler);
//    }

    /**
     * Ensures the existence of the handler manager.
     *
     * @return the handler manager
     **/
//    HandlerManager ensureHandlers() {
//        return manager == null ? manager = new HandlerManager(this)
//        : manager;
//    }

//    HandlerManager getManager() {
//        return manager;
//    }

//    public int getHandlerCount(GwtEvent.Type<?> type) {
//        return manager == null? 0 : manager.getHandlerCount(type);
//    }
//    
    // @formatter:on
    // Vaadin integration
/*	protected Map<String, Object> attributes = new HashMap<String, Object>();

	public void setAttribute(String attribute, Object value, boolean allowPostCreate)
	{
		attributes.put(attribute, value);
	}

	public void setAttribute(String attribute, Object value)
	{
		setAttribute(attribute, value, true);
	}

	public String getAttributeAsString(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return value.toString();
	}

	public String getAttribute(String attribute)
	{
		return getAttributeAsString(attribute);
	}

	public Integer getAttributeAsInt(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return Integer.valueOf(value.toString());
	}

	public Boolean getAttributeAsBoolean(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return Boolean.valueOf(value.toString());
	}

	public Double getAttributeAsDouble(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return Double.valueOf(value.toString());
	}

	public Float getAttributeAsFloat(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return Float.valueOf(value.toString());
	}

	
	public Map<?,?> getAttributeAsMap(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return (Map<?,?>) value;
	}

	public Date getAttributeAsDate(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
		{
			DateFormat df = new SimpleDateFormat();
			try
			{
				Date d = df.parse(value.toString());
				return d;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return null;
	}

	public String[] getAttributeAsStringArray(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return (String[]) value;
	}

	public String[] getAttributeAsJava(String attribute)
	{
		Object value = attributes.get(attribute);

		if (value == null)
			return null;
		else
			return (String[]) value;
	}

	public <T> T getAttributeAsObject(String attribute)
	{
		Object value = attributes.get(attribute);
		return (T) value;
	}
	
	public JavaScriptObject getAttributeAsJavaScriptObject(String property)
	{
		throw new IllegalStateException();
	} 
*/
}
