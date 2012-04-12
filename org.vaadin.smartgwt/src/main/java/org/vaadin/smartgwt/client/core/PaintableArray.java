package org.vaadin.smartgwt.client.core;

import java.util.HashSet;
import java.util.Set;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class PaintableArray implements PaintableProperty {
	private final Set<PaintableArrayListener> listeners = new HashSet<PaintableArrayListener>();
	private final String name;
	private Paintable[] paintables;

	public PaintableArray(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	public void addPaintableArrayListener(PaintableArrayListener listener) {
		listeners.add(listener);
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		final int childCount = uidl.getChildCount();

		if (childCount == 0) {
			fireOnChange(paintables, paintables = null);
		} else {
			final Paintable[] newPaintables = new Paintable[childCount];

			for (int i = 0; i < childCount; i++) {
				final UIDL elementUIDL = uidl.getChildUIDL(i);
				newPaintables[i] = client.getPaintable(elementUIDL);
				newPaintables[i].updateFromUIDL(elementUIDL, client);
			}

			fireOnChange(paintables, paintables = newPaintables);
		}
	}

	private void fireOnChange(Paintable[] oldPaintables, Paintable[] newPaintables) {
		if (oldPaintables != newPaintables) {
			for (PaintableArrayListener listener : listeners) {
				listener.onChange(oldPaintables, newPaintables);
			}
		}
	}
}
