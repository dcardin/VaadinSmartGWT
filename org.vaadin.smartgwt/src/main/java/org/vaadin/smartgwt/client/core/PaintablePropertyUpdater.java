package org.vaadin.smartgwt.client.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

/**
 * Manages client side paintable references that were registered on the server with ComponentPropertyPainter.  By default, paintables will be created when
 * updating from UIDL.  Further behavior can be defined through listener registration, like adding or removing paintables from their respective parent.
 */
public class PaintablePropertyUpdater {
	private final List<PaintableProperty> properties = new ArrayList<PaintableProperty>();

	/**
	 * Registers a listener to be notified when the specified property reference is modified.
	 * 
	 * @param propertyName the name of the property to watch.  it must match with the server-side property registration.
	 * @param listener the listener to notify.
	 */
	public void addPaintableReferenceListener(String propertyName, PaintableReferenceListener listener) {
		((PaintableReference) getOrCreateProperty(propertyName, "Reference")).addPaintableReferenceListener(listener);
	}

	/**
	 * Registers a listener to be notified when the specified list property elements are modified. 
	 * 
	 * @param propertyName the name of the property to watch.  it must match with the server-side property registration.
	 * @param listener the listener to be notified.
	 */
	public void addPaintableListListener(String propertyName, PaintableListListener listener) {
		((PaintableList) getOrCreateProperty(propertyName, "List")).addPaintableListListener(listener);
	}

	/**
	 * Equivalent of Paintable.updateFromUIDL.  It must be called when updating the client-side component that uses this instance.
	 * 
	 * @param uidl the client-side component uidl
	 * @param client the application connection
	 */
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		for (UIDL propertyUIDL : filterUIDLPropertyChildren(uidl.getChildIterator())) {
			final String name = propertyUIDL.getTag().substring(1);
			final String type = propertyUIDL.hasAttribute("type") ? propertyUIDL.getStringAttribute("type") : null;
			getOrCreateProperty(name, type).updateFromUIDL(propertyUIDL, client);
		}
	}

	private PaintableProperty getOrCreateProperty(String name, String type) {
		for (PaintableProperty property : properties) {
			if (name.equals(property.getName())) {
				return property;
			}
		}

		final PaintableProperty property = newPaintableProperty(name, type);
		properties.add(property);
		return property;
	}

	private static List<UIDL> filterUIDLPropertyChildren(Iterator<Object> childrenIterator) {
		final List<UIDL> children = new ArrayList<UIDL>();

		while (childrenIterator.hasNext()) {
			final Object next = childrenIterator.next();

			if (next instanceof UIDL && ((UIDL) next).getTag().startsWith("$")) {
				children.add((UIDL) next);
			}
		}

		return children;
	}

	private static PaintableProperty newPaintableProperty(final String name, final String type) {
		final PaintableProperty paintableProperty;

		if ("List".equals(type)) {
			paintableProperty = new PaintableList(name);

		} else if ("Reference".equals(type)) {
			paintableProperty = new PaintableReference(name);
		} else {
			throw new UnsupportedOperationException("unknow paintable property type.");
		}

		return paintableProperty;
	}
}