package org.vaadin.smartgwt.server.toolbar;

import org.vaadin.smartgwt.client.ui.toolbar.VToolStripResizer;
import org.vaadin.smartgwt.server.ImgSplitbar;

import com.vaadin.ui.ClientWidget;

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
 
/**
 * Simple subclass of ImgSplitbar with appearance appropriate for a ToolStrip resizer.
 */
@ClientWidget(value=VToolStripResizer.class)
public class ToolStripResizer extends ImgSplitbar {

//    public static ToolStripResizer getOrCreateRef(JavaScriptObject jsObj) {
//        if(jsObj == null) return null;
//        BaseWidget obj = BaseWidget.getRef(jsObj);
//        if(obj != null) {
//            return (ToolStripResizer) obj;
//        } else {
//            return new ToolStripResizer(jsObj);
//        }
//    }

    public ToolStripResizer(){
        scClassName = "ToolStripResizer";
    }

//    public ToolStripResizer(JavaScriptObject jsObj){
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

    /**
     * Image for horizontal resizer for a vertical Toolstrip
     *
     * @param hSrc hSrc Default value is "[SKIN]hresizer.png"
     */
    public void setHSrc(String hSrc) {
        setAttribute("hSrc", hSrc, true);
    }

    /**
     * Image for horizontal resizer for a vertical Toolstrip
     *
     *
     * @return String
     */
    public String getHSrc()  {
        return getAttributeAsString("hSrc");
    }

    /**
     * Path to resizer image.
     *
     * @param skinImgDir skinImgDir Default value is "images/ToolStrip/"
     * @throws IllegalStateException this property cannot be changed after the component has been created
     */
    public void setSkinImgDir(String skinImgDir)  throws IllegalStateException {
        setAttribute("skinImgDir", skinImgDir, false);
    }

    /**
     * Path to resizer image.
     *
     *
     * @return String
     */
    public String getSkinImgDir()  {
        return getAttributeAsString("skinImgDir");
    }

    /**
     * Image for resizer
     *
     * @param vSrc vSrc Default value is "[SKIN]resizer.png"
     */
    public void setVSrc(String vSrc) {
        setAttribute("vSrc", vSrc, true);
    }

    /**
     * Image for resizer
     *
     *
     * @return String
     */
    public String getVSrc()  {
        return getAttributeAsString("vSrc");
    }

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
     * @param toolStripResizerProperties properties that should be used as new defaults when instances of this class are created
     */
//    public static native void setDefaultProperties(ToolStripResizer toolStripResizerProperties) /*-{
//    	var properties = $wnd.isc.addProperties({},toolStripResizerProperties.@com.smartgwt.client.widgets.BaseWidget::getConfig()());
//    	delete properties.ID;
//        $wnd.isc.ToolStripResizer.addProperties(properties);
//    }-*/;
        
    // ***********************************************************        

}



