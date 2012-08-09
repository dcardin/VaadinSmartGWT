package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.layout.Layout;

/**
 * Abstract builder that allows creation of Layout instances by providing a fluent interface.  Regroups common properties for the Layout class.
 * 
 * @param <T> the Layout derivated class that this builder creates.
 * @param <B> the LayoutBuilder derivated class of this builder.
 */
public abstract class LayoutBuilder<T extends Layout, B extends LayoutBuilder<T, B>> extends CanvasBuilder<T, B> {
	protected LayoutBuilder(T instance) {
		super(instance);
	}

	/**
	 * see {@link Layout#setMembers(Canvas...)} 
	 */
	public B setMembers(Canvas... members) {
		instance().setMembers(members);
		return me();
	}
	
	/**
	 * see {@link Layout#setMembersMargin(int)} 
	 */
	public B setMembersMargin(int membersMargin) {
		instance().setMembersMargin(membersMargin);
		return me();
	}
}