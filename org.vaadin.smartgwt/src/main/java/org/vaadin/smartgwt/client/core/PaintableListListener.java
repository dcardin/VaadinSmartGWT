package org.vaadin.smartgwt.client.core;

import com.vaadin.terminal.gwt.client.Paintable;

public interface PaintableListListener
{
	void onAdd(Paintable[] source, Integer index, Paintable element);

	void onRemove(Paintable[] source, Integer index, Paintable element);
}