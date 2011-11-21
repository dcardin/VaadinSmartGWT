package org.vaadin.smartgwt.server.layout;

import java.util.Iterator;

import org.vaadin.smartgwt.server.Canvas;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Server side component for the VVLayout widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.layout.VMasterContainer.class)
public class MasterContainer extends Layout implements ComponentContainer {
	private static final long serialVersionUID = 1L;

	@Override
	public void addComponent(Component c) {
		if (c instanceof Canvas)
			addMember((Canvas) c);
	}

	@Override
	public void removeComponent(Component c) {
		if (c instanceof Canvas)
			removeMember((Canvas) c);
	}

	@Override
	public void removeAllComponents() {
		removeMembers(getMembers());
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent) {
		Canvas oldCanvas = (Canvas) oldComponent;
		Canvas newCanvas = (Canvas) newComponent;
		replaceComponent(oldCanvas, oldComponent);
	}

	@Override
	public Iterator<Component> getComponentIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void requestRepaintAll() {

	}

	@Override
	public void moveComponentsFrom(ComponentContainer source) {
		// TODO Auto-generated method stub
	}

	@Override
	public void addListener(ComponentAttachListener listener) {
	}

	@Override
	public void removeListener(ComponentAttachListener listener) {
	}

	@Override
	public void addListener(ComponentDetachListener listener) {
	}

	@Override
	public void removeListener(ComponentDetachListener listener) {
	}
}
