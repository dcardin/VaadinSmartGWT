package org.vaadin.smartgwt.server.core;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

public class ComponentReference<T extends Component> implements ComponentProperty {
	private final String tagName;
	private T value;

	public ComponentReference(String tagName) {
		this.tagName = tagName;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
	}

	public void paintContent(PaintTarget target) throws PaintException {
		target.startTag(tagName);
		target.addAttribute("type", "Reference");

		if (value != null) {
			value.paint(target);
		}

		target.endTag(tagName);
	}
}
