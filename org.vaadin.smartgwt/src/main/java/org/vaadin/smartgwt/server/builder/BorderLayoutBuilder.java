package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.layout.BorderLayout;

/**
 * Provides a fluent interface for building {@link BorderLayout} instances.
 */
public class BorderLayoutBuilder extends CanvasBuilder<BorderLayout, BorderLayoutBuilder> {
	/**
	 * Creates a new BorderLayoutBuilder.
	 *  
	 * @return a new BorderLayoutBuilder instance.
	 */
	public static BorderLayoutBuilder buildBorderLayout() {
		return new BorderLayoutBuilder(new BorderLayout());
	}

	BorderLayoutBuilder(BorderLayout instance) {
		super(instance);
	}

	/**
	 * see {@link BorderLayout#setNorthMember(Canvas)}
	 */
	public BorderLayoutBuilder setNorthMember(Canvas canvas) {
		instance().setNorthMember(canvas);
		return me();
	}

	/**
	 * see {@link BorderLayout#setSouthMember(Canvas)}
	 */
	public BorderLayoutBuilder setSouthMember(Canvas canvas) {
		instance().setSouthMember(canvas);
		return me();
	}

	/**
	 * see {@link BorderLayout#setWestMember(Canvas)} 
	 */
	public BorderLayoutBuilder setWestMember(Canvas canvas) {
		instance().setWestMember(canvas);
		return me();
	}

	/**
	 * see {@link BorderLayout#setEastMember(Canvas)}
	 */
	public BorderLayoutBuilder setEastMember(Canvas canvas) {
		instance().setEastMember(canvas);
		return me();
	}

	/**
	 * see {@link BorderLayout#setCenterMember(Canvas)}
	 */
	public BorderLayoutBuilder setCenterMember(Canvas canvas) {
		instance().setCenterMember(canvas);
		return me();
	}

	@Override
	protected BorderLayoutBuilder me() {
		return this;
	}
}