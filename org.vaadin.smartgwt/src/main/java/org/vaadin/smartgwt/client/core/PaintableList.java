package org.vaadin.smartgwt.client.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class PaintableList implements PaintableProperty {
	private final String name;
	private final List<Paintable> references = new ArrayList<Paintable>();
	private final Set<PaintableListListener> listeners = new HashSet<PaintableListListener>();

	public PaintableList(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		for (Iterator<Object> iterator = uidl.getChildIterator(); iterator.hasNext();) {
			final UIDL elementUIDL = (UIDL) iterator.next();

			if ("add".equals(elementUIDL.getTag())) {
				final Integer index = elementUIDL.hasAttribute("index") ? elementUIDL.getIntAttribute("index") : null;
				final Paintable element = elementUIDL.hasAttribute("element") ? elementUIDL.getPaintableAttribute("element", client) : null;

				if (element != null) {
					if (index == null) {
						references.add(element);
					} else {
						references.add(index, element);
					}

					fireOnAdd(index, element);
				}
			} else if ("remove".equals(elementUIDL.getTag())) {
				final Integer index = elementUIDL.hasAttribute("index") ? elementUIDL.getIntAttribute("index") : null;
				final Paintable element = elementUIDL.hasAttribute("element") ? elementUIDL.getPaintableAttribute("element", client) : null;

				if (element != null) {
					if (index == null) {
						references.remove(element);
					} else {
						references.remove(index.intValue());
					}

					fireOnRemove(index, element);
				}
			} else {
				client.getPaintable(elementUIDL).updateFromUIDL(elementUIDL, client);
			}
		}
	}

	public void addPaintableListListener(PaintableListListener listener) {
		listeners.add(listener);
	}

	private void fireOnAdd(Integer index, Paintable element) {
		for (PaintableListListener listener : listeners) {
			listener.onAdd(references.toArray(new Paintable[0]), index, element);
		}
	}

	private void fireOnRemove(Integer index, Paintable element) {
		for (PaintableListListener listener : listeners) {
			listener.onRemove(references.toArray(new Paintable[0]), index, element);
		}
	}
}
