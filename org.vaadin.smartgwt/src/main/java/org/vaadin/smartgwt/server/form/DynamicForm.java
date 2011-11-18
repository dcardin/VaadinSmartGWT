package org.vaadin.smartgwt.server.form;

import org.vaadin.smartgwt.server.layout.Layout;

/**
 * Server side component for the VDynamicForm widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.form.VDynamicForm.class)
public class DynamicForm extends Layout
{
	public int getNumCols()
	{
		return getAttributeAsInt("numCols");
	}

	public void setNumCols(int numCols)
	{
		setAttribute("numCols", numCols, false);
	}

}
