package org.vaadin.smartgwt;



/**
 * Server side component for the VDynamicForm widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VDynamicForm.class)
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
