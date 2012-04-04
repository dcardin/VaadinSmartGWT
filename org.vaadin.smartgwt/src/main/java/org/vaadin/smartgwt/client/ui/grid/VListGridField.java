package org.vaadin.smartgwt.client.ui.grid;

import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.vaadin.terminal.gwt.client.UIDL;

public class VListGridField extends VDataClass<ListGridField>
{
	public VListGridField()
	{
		super(new ListGridField());
	}

	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		if (uidl.hasAttribute("*cellFormatter"))
		{
			getJSObject().setAttribute("formatCellValue", JSOHelper.eval(uidl.getStringAttribute("*cellFormatter").substring(1)));
		}
	}
}