package org.vaadin.smartgwt.server.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

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

public abstract class BaseClass extends AbstractComponent {

    protected String id;
    protected JavaScriptObject config; //= JSOHelper.createObject();
    protected String scClassName;

//    public BaseClass() {
//        String id = SC.generateID(getClass().getName());
//        setID(id);`
//    }

//    protected BaseClass(JavaScriptObject jsObj) {
//        String nativeID = JSOHelper.getAttribute(jsObj, "ID");
//        this.id = nativeID;
//    }

    /**
     * Return the ID
     *
     * @return the ID
     */
    public String getID() {
        return id;
    }

    public void setID(String id) {
        
//        if (this.id != null) {
//            IDManager.unregisterID(this.id);
//        }
        
//        IDManager.registerID(id);
        setAttribute("ID", id, false);
        this.id = id;
    }

    /**
     * Get the name of the underlying SmartClient class
     *
     * @return the SmartClient class name
     */
    public String getScClassName() {
        return scClassName;
    }

    /**
     * Set the name of the underlying SmartClient class. This is an advanced setting.
     *
     * @param scClassName the SmartClient class
     */
    public void setScClassName(String scClassName) {
        this.scClassName = scClassName;
    }

    public JavaScriptObject getConfig() {
        return config;
    }

//    public native boolean isCreated()/*-{
//        var id = this.@com.smartgwt.client.core.BaseClass::getID()();
//        var obj = $wnd.window[id];
//        // && $wnd.isc.isA.Canvas(obj) === true
//        return id != null && obj != null && obj !== undefined;
//    }-*/;

    public native JavaScriptObject getJsObj()/*-{
        var id = this.@com.smartgwt.client.core.BaseClass::getID()();
        if($wnd.window[id] != null && $wnd.window[id]!== undefined) {
            return $wnd.window[id];
        } else {
            return null;
        }
    }-*/;

//    public JavaScriptObject getOrCreateJsObj() {
//        if (!isCreated()) {
//            JavaScriptObject jsObj = create();
//            JSOHelper.setAttribute(jsObj, SC.REF, this);
//            onInit();            
//            return jsObj;
//        } else {
//            return getJsObj();            
//        }
//    }

//    public static BaseClass getRef(JavaScriptObject jsObj) {
//        return jsObj == null ? null : (BaseClass) JSOHelper.getAttributeAsObject(jsObj, SC.REF);
//    }
	
	/**
	 * Destroy this object.
	 */
    public native void destroy()/*-{
		var self = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
		var ID = this.@com.smartgwt.client.core.BaseClass::getID()();
		if (self != null && self.destroy) self.destroy();
		if (ID != null) {
		    @com.smartgwt.client.util.IDManager::unregisterID(Ljava/lang/String;)(ID);
		}
	}-*/;	

    protected void error(String attribute, String value, boolean allowPostCreate) throws IllegalStateException {
        if (allowPostCreate) {
            error("Cannot change configuration property '" + attribute + "' to " + value + " after the component has been rendered.");
        } else {
            error("Cannot change configuration property '" + attribute + "' to " + value + " after the component has been created.");
        }
    }

    protected void errorIfNotCreated(String property) throws IllegalStateException {
        if (!isCreated()) {
            throw new IllegalStateException("Cannot access property " + property + " before the component has been created.");
        }
    }

    protected void error(String message) throws IllegalStateException {
//        if (!GWT.isScript()) {
//            Window.alert("Error :" + message);
            throw new IllegalStateException(message);
//        }
    }

//    protected abstract JavaScriptObject create();

//    protected void onInit() {}


 
//    public String getAttribute(String attribute) {
//        return getAttributeAsString(attribute);
//    }
    
//    public native String getAttributeAsString(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret === undefined ? null : ret;
//    }-*/;
//
//    public native Date getAttributeAsDate(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//    *    return ret == null || ret === undefined ? null : @com.smartgwt.client.util.JSOHelper::toDate(D)(ret.getTime());
//    }-*/;
//
//    public native Integer getAttributeAsInt(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret == null || ret === undefined ? null : @com.smartgwt.client.util.JSOHelper::toInteger(I)(ret);
//    }-*/;
//
//    public native Double getAttributeAsDouble(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret == null || ret === undefined ? null : @com.smartgwt.client.util.JSOHelper::toDouble(D)(ret);
//    }-*/;
//
//    public native Element getAttributeAsElement(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret === undefined ? null : ret;
//    }-*/;
//
//    public native JavaScriptObject getAttributeAsJavaScriptObject(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret === undefined ? null : ret;
//    }-*/;
//
//    public native Float getAttributeAsFloat(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret == null || ret === undefined ? null : @com.smartgwt.client.util.JSOHelper::toFloat(F)(ret);
//    }-*/;
//
//    public native Boolean getAttributeAsBoolean(String property)/*-{
//        var ret;
//        if(this.@com.smartgwt.client.core.BaseClass::isCreated()()) {
//            var widget = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//            ret = widget.getProperty(property);
//        } else {
//            var config = this.@com.smartgwt.client.core.BaseClass::config;
//            if(config[property] != undefined) {
//                ret = config[property];
//            } else {
//               var scClassName = this.@com.smartgwt.client.core.BaseClass::scClassName;
//               ret = eval("($wnd.isc." + scClassName + ".getInstanceProperty('" + property + "'))")
//            }
//        }
//        return ret == null || ret === undefined ? null : @com.smartgwt.client.util.JSOHelper::toBoolean(Z)(ret);
//    }-*/;
//
//    public Map getAttributeAsMap(String property) {
//        return JSOHelper.getAttributeAsMap(getOrCreateJsObj(), property);
//    }
//
//
//    public void setAttribute(String attribute, String value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, value, allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, Boolean value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, value.toString(), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, Map value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, JSOHelper.convertMapToJavascriptObject(value));
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertMapToJavascriptObject(value));
//        } else {
//            error(attribute, value.toString(), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, int[] value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else {
//            error(attribute, value.toString(), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, BaseClass[] value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else {
//            error(attribute, value.toString(), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, DataClass[] value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else {
//            error(attribute, value.toString(), allowPostCreate);
//        }
//    }
//
//
//    public void setAttribute(String attribute, double value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, int value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, Date value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertToJavaScriptDate(value));
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, ValueEnum[] value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, DataClass value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value.getJsObj());
//        } else if (allowPostCreate) {
//            setProperty(attribute, value.getJsObj());
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, JavaScriptObject value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, String[] value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else if (allowPostCreate) {
//            setProperty(attribute, JSOHelper.convertToJavaScriptArray(value));
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    protected void setAttribute(String attribute, boolean value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public void setAttribute(String attribute, Element value, boolean allowPostCreate) {
//        if (!isCreated()) {
//            JSOHelper.setAttribute(config, attribute, value);
//        } else if (allowPostCreate) {
//            setProperty(attribute, value);
//        } else {
//            error(attribute, String.valueOf(value), allowPostCreate);
//        }
//    }
//
//    public native void setProperty(String property, String value)/*-{
//        var self = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//        self.setProperty(property, value);
//    }-*/;
//
//    public native void setProperty(String property, boolean value)/*-{
//        var self = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//        self.setProperty(property, value);
//    }-*/;
//
//    public native void setProperty(String property, double value)/*-{
//        var self = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//        self.setProperty(property, value);
//    }-*/;
//
//    public native void setProperty(String property, JavaScriptObject value)/*-{
//        var self = this.@com.smartgwt.client.core.BaseClass::getJsObj()();
//        self.setProperty(property, value);
//    }-*/;

    //event handling ode
    private HandlerManager manager = null;

    public void fireEvent(GwtEvent<?> event) {
        if (manager != null) {
            manager.fireEvent(event);
        }
    }

    protected final <H extends EventHandler> HandlerRegistration doAddHandler(
           final H handler, GwtEvent.Type<H> type) {
        return ensureHandlers().addHandler(type, handler);
    }

    /**
     * Ensures the existence of the handler manager.
     *
     * @return the handler manager
     **/
    HandlerManager ensureHandlers() {
        return manager == null ? manager = new HandlerManager(this)
        : manager;
    }

    HandlerManager getManager() {
        return manager;
    }

    public int getHandlerCount(GwtEvent.Type<?> type) {
        return manager == null? 0 : manager.getHandlerCount(type);
    }

    // ------------ Vaadin integration methods
    
	private static final long serialVersionUID = 1L;
	protected Map<String, Object> attributes = new HashMap<String, Object>();
	private boolean isCreated = false;

	public Object getOrCreateJsObj() {
		return this;
	}
		
	protected boolean isCreated()
	{
		return isCreated;
	}

	public void setAttribute(String attribute, Object value, boolean allowPostCreate)
	{
		if (isCreated() && !allowPostCreate)
		{
			throw new IllegalArgumentException("Cannot modify property " + attribute + " once created");
		}

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
	
	public JavaScriptObject getAttributeAsJavaScriptObject(String property)
	{
		throw new IllegalStateException();
	}


	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		for (Map.Entry<String, Object> entry : attributes.entrySet())
		{
			Object value = entry.getValue();
			String name = entry.getKey();

			if (value == null)
				target.addAttribute(name, "null");

			else if (value instanceof Boolean)
			{
				target.addAttribute(name, "b" + String.valueOf(value));
			}
			else if (value instanceof Integer)
			{
				target.addAttribute(name, "i" + String.valueOf(value));
			}
			else if (value instanceof Float)
			{
				target.addAttribute(name, "f" + String.valueOf(value));
			}
			else if (value instanceof Long)
			{
				target.addAttribute(name, "l" + String.valueOf(value));
			}
			else if (value instanceof Double)
			{
				target.addAttribute(name, "d" + String.valueOf(value));
			}
			else if (value instanceof String)
			{
				target.addAttribute(name, "s" + String.valueOf(value));
			}
		}

		// Since the paint is finished, set the created attribute
		isCreated = true;
	}
}

