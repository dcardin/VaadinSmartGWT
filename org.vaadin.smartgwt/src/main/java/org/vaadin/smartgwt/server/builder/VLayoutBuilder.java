package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.layout.VLayout;

/**
 * Provides a fluent interface for building {@link VLayout} instances. 
 */
public class VLayoutBuilder extends LayoutBuilder<VLayout, VLayoutBuilder> {
	/**
	 * Creates a new {@link VLayoutBuilder}.
	 * 
	 * @return a {@link VLayoutBuilder} instance.
	 */
	public static VLayoutBuilder buildVLayout() {
		return new VLayoutBuilder(new VLayout());
	}

	VLayoutBuilder(VLayout instance) {
		super(instance);
	}

	@Override
	protected VLayoutBuilder me() {
		return this;
	}
}