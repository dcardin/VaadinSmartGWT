package org.vaadin.smartgwt.client.ui.grid;

import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VListGridField extends VDataClass<ListGridField>
{
	public VListGridField()
	{
		super(new ListGridField());
	}

	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		if (uidl.hasAttribute("*cellFormatter"))
		{
			getJSObject().setAttribute("formatCellValue", JSOHelper.eval(uidl.getStringAttribute("*cellFormatter").substring(1)));
		}
	}
}