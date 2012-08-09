package org.vaadin.smartgwt.server.builder;

import org.vaadin.smartgwt.server.BaseWidget;

/**
 * Abstract builder that allows creation of BaseWidget instances by providing a fluent interface.  Regroups common properties for the BaseWidget class.
 * 
 * @param <T> the BaseWidget derivated class that this builder creates.
 * @param <B> the BaseWidgetBuilder derivated class of this builder.
 */
public abstract class BaseWidgetBuilder<T extends BaseWidget, B extends BaseWidgetBuilder<T, B>> {
	private final T instance;

	/**
	 * Builds a new builder for the instance.
	 * 
	 * @param instance being built.
	 */
	protected BaseWidgetBuilder(T instance) {
		this.instance = instance;
	}

	/**
	 * To be called to retrieve the built instance once done with the builder.
	 * 
	 * @return the built instance.
	 */
	public T build() {
		return instance();
	}

	/**
	 * The instance being built.
	 * 
	 * @return the instance beign built.
	 */
	protected T instance() {
		return instance;
	}

	/**
	 * The builder instance (usually you would return 'this' here).
	 * 
	 * @return the builder instance.
	 */
	protected abstract B me();
}