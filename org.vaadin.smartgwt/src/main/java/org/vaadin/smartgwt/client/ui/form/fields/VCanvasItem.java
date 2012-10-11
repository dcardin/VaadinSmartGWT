package org.vaadin.smartgwt.client.ui.form.fields;

import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.PaintableReferenceListener;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.form.fields.CanvasItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VCanvasItem extends VAbstractFormItem<CanvasItem, String> {

	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VCanvasItem() {
		super(new CanvasItem());

		propertyUpdater.addPaintableReferenceListener("canvas", new PaintableReferenceListener() {

			@Override
			public void onChange(Paintable paintable) {
				getJSObject().setCanvas((Canvas) paintable);
			}
		});
	}

	@Override
	protected String getUIDLFormItemValue(UIDL uidl, String attributeName) {
		return uidl.getStringAttribute(attributeName);
	}

	@Override
	protected String getFormItemValue() {
		return getJSObject().getValue() == null ? null : getJSObject().getValue().toString();
	}

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client) {
		propertyUpdater.updateFromUIDL(uidl, client);
		super.postAttributeUpdateFromUIDL(uidl, client);
	}
}
