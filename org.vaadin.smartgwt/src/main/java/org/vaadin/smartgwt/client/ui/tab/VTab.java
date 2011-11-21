package org.vaadin.smartgwt.client.ui.tab;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.tab.Tab;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTab extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;
	private final Tab tab;

	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	public VTab()
	{
		super();
		tab = new Tab();
	}

	/**
	 * Called whenever an update is received from the server
	 */
	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.paintChildren(uidl, client);
		PainterHelper.updateDataObject(client, tab, uidl);
	}

	@Override
	public Tab unwrap()
	{
		return tab;
	}

}
