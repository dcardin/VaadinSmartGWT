package org.vaadin.smartgwt;

import java.util.Map;

/**
 * Server side component for the VTextItem widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VTextItem.class)
public class TextItem extends FormItem
{
	public TextItem()
	{
		super("");
	}
	
	public TextItem(String name)
	{
		super(name);
	}

	/**
	 * Receive and handle events and other variable changes from the client.
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void changeVariables(Object source, Map<String, Object> variables)
	{
		super.changeVariables(source, variables);

		if (variables.containsKey("value"))
			setAttribute("value", variables.get("value"));

	}

}
