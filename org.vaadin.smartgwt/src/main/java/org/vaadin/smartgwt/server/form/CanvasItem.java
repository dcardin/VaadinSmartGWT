package org.vaadin.smartgwt.server.form;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.core.ComponentPropertyPainter;
import org.vaadin.smartgwt.server.core.ComponentReference;
import org.vaadin.smartgwt.server.form.fields.FormItem;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

//@formatter:off
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.form.fields.VCanvasItem.class)
public class CanvasItem extends FormItem {

	private final ComponentPropertyPainter propertyPainter = new ComponentPropertyPainter(this);
	private final ComponentReference<Canvas> canvas = propertyPainter.addProperty("canvas");

	public CanvasItem() {
		setAttribute("editorType", "CanvasItem");
	}

	public CanvasItem(String name) {
		setName(name);
		setAttribute("editorType", "CanvasItem");
	}

	public CanvasItem(String name, String title) {
		setName(name);
		setTitle(title);
		setAttribute("editorType", "CanvasItem");
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		propertyPainter.paintContent(target);
		super.paintContent(target);
	}

	/**
	 * The canvas that will be displayed inside this item.  You can pass an instance you've  already created, or its global ID
	 * as a String. You can also implement  {@link com.smartgwt.client.widgets.form.fields.CanvasItem#createCanvas
	 * CanvasItem.createCanvas} to dynamically create the canvas when the FormItem is initialized. <P> If <code>canvas</code>
	 * and <code>createCanvas()</code> are unspecified, the  canvas for this item will be auto-created using the overrideable
	 * defaults: {@link com.smartgwt.client.widgets.form.fields.CanvasItem#getCanvasProperties canvasProperties} and {@link
	 * com.smartgwt.client.widgets.form.fields.CanvasItem#getCanvasConstructor canvasConstructor} <P> Note that {@link
	 * com.smartgwt.client.widgets.Canvas#getCanvasItem canvasItem} will be set on the canvas to point back to this item.
	 *
	 * @param canvas canvas Default value is null
	 */
	//	public void setCanvas(Canvas canvas) {
	//		setAttribute("canvas", canvas == null ? null : canvas.getOrCreateJsObj());
	//	}

	public void setCanvas(Canvas canvas) {
		this.canvas.set(canvas);
	}

	public Canvas getCanvas() {
		return this.canvas.get();
	}

	
    protected native void setupCanvasConstructor() /*-{
	    var self = this.@com.smartgwt.client.widgets.form.fields.CanvasItem::getJsObj()();
	    if(self == null) return null;
	    self.createCanvas = $debox($entry(function() {
	        
	        //in cases where a SGWT FormItem instance is used for a setEditorType(..) call, there will
	        //not be a SGWT object ref associated with the JS object. In this case, simply return
	        // Note the check for 'getCanavsItemRef' rather than just getRef ensures the Java Object
	        // is actually a CanvasItem (rather than a generic "FormItem" instance)
	        var jObj = @com.smartgwt.client.widgets.form.fields.CanvasItem::getCanvasItemRef(Lcom/google/gwt/core/client/JavaScriptObject;)(this);
	        
	        if(jObj == null) return this.canvas;
	        var jCanvas = jObj.@com.smartgwt.client.widgets.form.fields.CanvasItem::createCanvas()();
	        if (jCanvas != null) return jCanvas.@com.smartgwt.client.widgets.Canvas::getOrCreateJsObj()();
	        return this.canvas;
	    }));
	}-*/;  

}
