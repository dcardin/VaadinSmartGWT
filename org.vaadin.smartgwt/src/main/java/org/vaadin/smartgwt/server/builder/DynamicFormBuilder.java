package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.form.DynamicForm;
import org.vaadin.smartgwt.server.form.fields.FormItem;

/**
 * Provides a fluent interface for building {@link DynamicForm} instances.
 */
public class DynamicFormBuilder extends CanvasBuilder<DynamicForm, DynamicFormBuilder> {
	/**
	 * Creates a new DynamicFormBuilder.
	 * 
	 * @return a DynamicFormBuilder.
	 */
	public static DynamicFormBuilder buildDynamicForm() {
		return new DynamicFormBuilder(new DynamicForm());
	}

	DynamicFormBuilder(DynamicForm instance) {
		super(instance);
	}

	@Override
	protected DynamicFormBuilder me() {
		return this;
	}

	/**
	 * see {@link DynamicForm#setNumCols(int)} 
	 */
	public DynamicFormBuilder setNumCols(int numCols) {
		instance().setNumCols(numCols);
		return me();
	}

	/**
	 * see DynamicForm#setAutoFocus(Boolean)
	 */
	public DynamicFormBuilder setAutoFocus(Boolean autoFocus) {
		instance().setAutoFocus(autoFocus);
		return me();
	}

	/**
	 * see {@link DynamicForm#setFields(FormItem...)} 
	 */
	public DynamicFormBuilder setFields(FormItem... fields) {
		instance().setFields(fields);
		return me();
	}
}
