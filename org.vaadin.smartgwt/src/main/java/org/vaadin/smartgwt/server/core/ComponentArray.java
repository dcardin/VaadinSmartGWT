package org.vaadin.smartgwt.server.core;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;
import com.vaadin.ui.Component;

public class ComponentArray<T extends Component> implements ComponentProperty {
	private final Component parent;
	private final String tagName;
	private T[] components;

	public ComponentArray(Component parent, String tagName) {
		this.parent = parent;
		this.tagName = tagName;
	}

	public Object getTagName() {
		return tagName;
	}

	public T[] get() {
		return components;
	}

	public void set(T[] components) {
		if (this.components != null) {
			for (T component : this.components) {
				component.setParent(null);
			}
		}

		this.components = components;

		if (this.components != null) {
			for (T component : this.components) {
				component.setParent(parent);
			}
		}
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		target.startTag(tagName);
		target.addAttribute("type", "Array");

		if (components != null) {
			for (Paintable paintable : components) {
				paintable.paint(target);
			}
		}

		target.endTag(tagName);
	}
}
