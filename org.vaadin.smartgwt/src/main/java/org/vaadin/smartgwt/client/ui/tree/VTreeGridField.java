package org.vaadin.smartgwt.client.ui.tree;

import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.tree.TreeGridField;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VTreeGridField extends VDataClass<TreeGridField>
{
	public VTreeGridField()
	{
		super(new TreeGridField());
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