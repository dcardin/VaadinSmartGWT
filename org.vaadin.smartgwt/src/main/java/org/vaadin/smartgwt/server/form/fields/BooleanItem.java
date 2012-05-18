package org.vaadin.smartgwt.server.form.fields;

public class BooleanItem extends FormItem {
	public BooleanItem() {
		setType("boolean");
	}

	public BooleanItem(String name) {
		setName(name);
		setType("boolean");
	}

	public BooleanItem(String name, String title) {
		setName(name);
		setTitle(title);
		setType("boolean");
	}

	/**
	 * Return the value tracked by this form item.
	 *
	 * @return value of this element
	 */
	public Boolean getValueAsBoolean() {
		return (Boolean) getValue();
	}
}
