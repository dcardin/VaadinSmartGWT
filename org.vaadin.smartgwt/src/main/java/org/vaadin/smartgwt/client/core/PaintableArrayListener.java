package org.vaadin.smartgwt.client.core;

import com.vaadin.terminal.gwt.client.Paintable;

public interface PaintableArrayListener {
	void onChange(Paintable[] oldPaintables, Paintable[] newPaintables);
}
