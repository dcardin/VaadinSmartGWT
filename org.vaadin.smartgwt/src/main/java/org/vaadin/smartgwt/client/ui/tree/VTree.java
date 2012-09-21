package org.vaadin.smartgwt.client.ui.tree;

import org.vaadin.smartgwt.client.core.VBaseClass;

import com.smartgwt.client.widgets.tree.Tree;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTree extends VBaseClass<Tree> {
	public VTree() {
		super(new Tree());
	}

	@Override
	protected void preAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.preAttributeUpdateFromUIDL(uidl, client);
	}
}