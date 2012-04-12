package org.vaadin.smartgwt.server.core;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

/**
 * Server side class that manages painting of component references and lists. Application attachment of components is managed internally, so it is
 * not necessary to update those states manually.
 * 
 * PaintablePropertyUpdater is the client-side counterpart to this class.
 */
public class ComponentPropertyPainter {
	private final Set<ComponentProperty> properties = Sets.newLinkedHashSet();
	private final Component parent;

	/**
	 * Instantiate a new ComponentPropertyPainter linked to the specified parent component.
	 * 
	 * @param parent the parent component to be used to manage application attachment.
	 */
	public ComponentPropertyPainter(Component parent) {
		this.parent = parent;
	}

	public Set<ComponentProperty> getComponentProperties() {
		return new HashSet<ComponentProperty>(properties);
	}

	/**
	 * Creates a new component reference that will be managed on paint.
	 * 
	 * @param propertyName the unique registration property name.  the same name must be used on the client-side.
	 * @return the registered reference.
	 */
	public <T extends Component> ComponentReference<T> addProperty(String propertyName) {
		final ComponentReference<T> reference = new ComponentReference<T>(parent, "$" + propertyName);
		properties.add(reference);
		return reference;
	}

	public <T extends Component> ComponentArray<T> addComponentArray(String propertyName) {
		final ComponentArray<T> array = new ComponentArray<T>(parent, "$" + propertyName);
		properties.add(array);
		return array;
	}

	/**
	 * Creates a new component list that will be managed on paint.
	 * 
	 * @param propertyName the unique registration property name.  the same name must be used on the client-side.
	 * @return the registered component list.
	 */
	public <T extends Component> ComponentList<T> addComponentList(String propertyName) {
		final ComponentList<T> components = new ComponentList<T>(parent, "$" + propertyName);
		properties.add(components);
		return components;
	}

	/**
	 * Equivalent to Paintable.paint(PaintTarget) that must be called when painting the linked parent component.
	 * 
	 * @param target the parent component target.
	 * @throws PaintException when a exception occurs during painting.
	 */
	public void paintContent(PaintTarget target) throws PaintException {
		for (ComponentProperty property : properties) {
			property.paintContent(target);
		}
	}
}
