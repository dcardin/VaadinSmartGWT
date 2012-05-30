package org.vaadin.smartgwt.server.layout;
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
 
import org.vaadin.smartgwt.client.ui.layout.VLayoutSpacer;
import org.vaadin.smartgwt.server.Canvas;

import com.vaadin.ui.ClientWidget;

/**
 * Add a LayoutSpacer to a Layout to take up space just like a normal member, without actually drawing anything. 
 * Semantically equivalent to using an empty canvas, but higher performance for this particular use case.
 */
@ClientWidget(VLayoutSpacer.class)
public class LayoutSpacer extends Canvas {

//    public static LayoutSpacer getOrCreateRef(JavaScriptObject jsObj) {
//        if(jsObj == null) return null;
//        BaseWidget obj = BaseWidget.getRef(jsObj);
//        if(obj != null) {
//            return (LayoutSpacer) obj;
//        } else {LayoutSpacer
//            return new LayoutSpacer(jsObj);
//        }
//    }

    public LayoutSpacer(){
        scClassName = "LayoutSpacer";
    }

//    public LayoutSpacer(JavaScriptObject jsObj){
//        super(jsObj);
//    }

//    protected native JavaScriptObject create()/*-{
//        var config = this.@com.smartgwt.client.widgets.BaseWidget::getConfig()();
//        var scClassName = this.@com.smartgwt.client.widgets.BaseWidget::scClassName;
//        var widget = $wnd.isc[scClassName].create(config);
//        this.@com.smartgwt.client.widgets.BaseWidget::doInit()();
//        return widget;
//    }-*/;
    // ********************* Properties / Attributes ***********************

    // ********************* Methods ***********************

    // ********************* Static Methods ***********************
    /**
     * Class level method to set the default properties of this class. If set, then all subsequent instances of this
     * class will automatically have the default properties that were set when this method was called. This is a powerful
     * feature that eliminates the need for users to create a separate hierarchy of subclasses that only alter the default
     * properties of this class. Can also be used for skinning / styling purposes.
     * <P>
     * <b>Note:</b> This method is intended for setting default attributes only and will effect all instances of the
     * underlying class (including those automatically generated in JavaScript). 
     * This method should not be used to apply standard EventHandlers or override methods for
     * a class - use a custom subclass instead.
     *
     * @param layoutSpacerProperties properties that should be used as new defaults when instances of this class are created
     */
    public static native void setDefaultProperties(LayoutSpacer layoutSpacerProperties) /*-{
    	var properties = $wnd.isc.addProperties({},layoutSpacerProperties.@com.smartgwt.client.widgets.BaseWidget::getConfig()());
    	delete properties.ID;
        $wnd.isc.LayoutSpacer.addProperties(properties);
    }-*/;
        
    // ***********************************************************        

}







