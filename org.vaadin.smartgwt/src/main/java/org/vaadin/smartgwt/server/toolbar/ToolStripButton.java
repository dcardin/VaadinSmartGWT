package org.vaadin.smartgwt.server.toolbar;

import org.vaadin.smartgwt.server.StretchImgButton;



/**
 * Simple button subclass of StretchImgButton with appearance appropriate for a ToolStrip button.
 * Can be used to create an icon-only button, and icon with text, or a text only button by setting the
 * icon and title attibutes as required.
 *
 * @see ToolStrip#addButton(ToolStripButton)
 */
public class ToolStripButton extends StretchImgButton {

//    public static ToolStripButton getOrCreateRef(JavaScriptObject jsObj) {
//        if(jsObj == null) return null;
//        BaseWidget obj = BaseWidget.getRef(jsObj);
//        if(obj != null) {
//            return (ToolStripButton) obj;
//        } else {
//            return new ToolStripButton(jsObj);
//        }
//    }

    public ToolStripButton(){
        scClassName = "ToolStripButton";
    }

    /**
     * Constructor.
     * @param title title of the button
     */
    public ToolStripButton(String title) {
        this();
        setTitle(title);
    }

    /**
     * Constructor.
     * @param title title of the button
     * @param icon the button icon
     */
    public ToolStripButton(String title, String icon) {
        this();
        setTitle(title);
        setIcon(icon);
    }

//    public ToolStripButton(JavaScriptObject jsObj){
//        super(jsObj);
//    }

//    protected native JavaScriptObject create()/*-{
//        var config = this.@com.smartgwt.client.widgets.BaseWidget::getConfig()();
//        var widget = $wnd.isc.ToolStripButton.create(config);
//        this.@com.smartgwt.client.widgets.BaseWidget::doInit()();
//        return widget;
//    }-*/;
}
