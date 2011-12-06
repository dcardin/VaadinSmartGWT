package org.vaadin.smartgwt.client.ui.grid;

import org.vaadin.smartgwt.client.ui.layout.VMasterContainer;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;
import org.vaadin.smartgwt.client.ui.utils.Wrapper;

import com.google.gwt.user.client.Element;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;


public class VListGridField extends Canvas implements Paintable, Wrapper
{
	protected String paintableId;
	protected ApplicationConnection client;
	private ListGridField lgf;
	
	public VListGridField()
	{
		super();
		lgf = new ListGridField();
	}
	
	@Override
	public Element getElement()
	{
		return VMasterContainer.getDummy();
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		this.client = client;
		paintableId = uidl.getId();

		PainterHelper.paintChildren(uidl, client);
		PainterHelper.updateDataObject(client, lgf, uidl);

		if (uidl.hasAttribute("*cellFormatter"))
		{
			lgf.setAttribute("formatCellValue", JSOHelper.eval(uidl.getStringAttribute("*cellFormatter").substring(1)));
		}
	}

	@Override
	public ListGridField unwrap()
	{
		return lgf;
	}
}