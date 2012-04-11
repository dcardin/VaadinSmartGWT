package org.vaadin.smartgwt.server.core;

import java.util.Set;

import com.google.common.collect.Sets;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;
import com.vaadin.ui.Component;

public class ComponentPropertyPainter {
	private final Set<PaintableProperty> properties = Sets.newLinkedHashSet();
	private final Component parent;

	public ComponentPropertyPainter(Component parent) {
		this.parent = parent;
	}

	public <T extends Paintable> PaintableReference<T> addProperty(String propertyName) {
		final PaintableReference<T> reference = new PaintableReference<T>("$" + propertyName);
		properties.add(reference);
		return reference;
	}

	public <T extends Component> ComponentList<T> addComponentList(String propertyName) {
		final ComponentList<T> components = new ComponentList<T>(parent, "$" + propertyName);
		properties.add(components);
		return components;
	}

	public void paintContent(PaintTarget target) throws PaintException {
		for (PaintableProperty property : properties) {
			property.paintContent(target);
		}
	}
}
