package org.vaadin.smartgwt.client.ui.form.fields;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VFormItem extends VAbstractFormItem<FormItem, String> {
	public VFormItem() {
		super(new FormItem());
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
		final String name = getJSObject().getName();
		final DynamicForm form = getJSObject().getForm();

		if (form != null) {
			if (uidl.hasAttribute("*errorMessages") && "null".equals(uidl.getStringArrayAttribute("*errorMessages"))) {
				form.setFieldErrors(name, uidl.getStringArrayAttribute("*errorMessages"), true);
			} else {
				form.clearFieldErrors(name, true);
			}
		}
	}
}
