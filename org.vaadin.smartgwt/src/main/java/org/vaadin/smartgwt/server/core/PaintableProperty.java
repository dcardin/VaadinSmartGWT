package org.vaadin.smartgwt.server.core;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public interface PaintableProperty {
	void paintContent(PaintTarget target) throws PaintException;
}
