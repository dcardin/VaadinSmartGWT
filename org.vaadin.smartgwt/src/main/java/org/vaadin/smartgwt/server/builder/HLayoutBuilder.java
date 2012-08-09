package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.layout.HLayout;

/**
 * Provides a fluent interface for building {@link HLayout} instances.
 */
public class HLayoutBuilder extends LayoutBuilder<HLayout, HLayoutBuilder> {
	/**
	 * Creates a new {@link HLayoutBuilder}.
	 * 
	 * @return a new {@link HLayoutBuilder}.
	 */
	public static HLayoutBuilder buildHLayout() {
		return new HLayoutBuilder(new HLayout());
	}

	HLayoutBuilder(HLayout instance) {
		super(instance);
	}

	@Override
	protected HLayoutBuilder me() {
		return this;
	}
}