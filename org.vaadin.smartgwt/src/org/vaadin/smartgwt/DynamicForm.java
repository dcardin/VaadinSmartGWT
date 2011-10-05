package org.vaadin.smartgwt;


import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VDynamicForm widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VDynamicForm.class)
public class DynamicForm extends Container
{
	private int numCols = 1;
	
	
	public int getNumCols()
	{
		return numCols;
	}


	public void setNumCols(int numCols)
	{
		this.numCols = numCols;
	}


	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		if (numCols > 0)
			target.addAttribute("numCols", numCols);
		
	}

}
