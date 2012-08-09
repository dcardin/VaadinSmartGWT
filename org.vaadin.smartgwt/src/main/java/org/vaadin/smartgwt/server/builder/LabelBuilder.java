package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.Label;

/**
 * Provides a fluent interface to build {@link Label} instances. 
 */
public class LabelBuilder extends CanvasBuilder<Label, LabelBuilder> {
	/**
	 * Creates a new {@link LabelBuilder} that will build a label with the specified contents.
	 * 
	 * @param contents of the label
	 * @return a new {@link LabelBuilder}
	 */
	public static LabelBuilder buildLabel(String contents) {
		return new LabelBuilder(new Label(contents));
	}

	LabelBuilder(Label instance) {
		super(instance);
	}

	@Override
	protected LabelBuilder me() {
		return this;
	}
}