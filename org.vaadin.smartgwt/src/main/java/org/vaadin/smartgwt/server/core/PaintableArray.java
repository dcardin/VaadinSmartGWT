package org.vaadin.smartgwt.server.core;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.terminal.Paintable;

public class PaintableArray<T extends Paintable> implements ComponentProperty {
	private final String tagName;
	private T[] paintables;

	public PaintableArray(String tagName) {
		this.tagName = tagName;
	}

	public PaintableArray(String tagName, T... paintables) {
		this.tagName = tagName;
		this.paintables = paintables;
	}

	public Object getTagName() {
		return tagName;
	}

	public T[] get() {
		return paintables;
	}

	public void set(T[] paintables) {
		this.paintables = paintables;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		target.startTag(tagName);
		target.addAttribute("type", "Array");

		if (paintables != null) {
			for (Paintable paintable : paintables) {
				paintable.paint(target);
			}
		}

		target.endTag(tagName);
	}
}
