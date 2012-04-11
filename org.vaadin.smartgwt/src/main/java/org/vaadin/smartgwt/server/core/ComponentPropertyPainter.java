package org.vaadin.smartgwt.server.core;

import java.util.Set;

import com.google.common.collect.Sets;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

public class ComponentPropertyPainter {
	private final Set<ComponentProperty> properties = Sets.newLinkedHashSet();
	private final Component parent;

	public ComponentPropertyPainter(Component parent) {
		this.parent = parent;
	}

	public <T extends Component> ComponentReference<T> addProperty(String propertyName) {
		final ComponentReference<T> reference = new ComponentReference<T>("$" + propertyName);
		properties.add(reference);
		return reference;
	}

	public <T extends Component> ComponentList<T> addComponentList(String propertyName) {
		final ComponentList<T> components = new ComponentList<T>(parent, "$" + propertyName);
		properties.add(components);
		return components;
	}

	public void paintContent(PaintTarget target) throws PaintException {
		for (ComponentProperty property : properties) {
			property.paintContent(target);
		}
	}
}
