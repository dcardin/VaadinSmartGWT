package org.vaadin.smartgwt;

import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * Server side component for the VTextItem widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VTextItem.class)
public class TextItem extends BaseComponent
{
	private String message = "Click here.";
	private int clicks = 0;
	private String title;
	private String value;

	public String getValue()
	{
		return value;
	}

	public void setValue(String value)
	{
		this.value = value;
	}

	public TextItem()
	{
		setSizeUndefined();
	}
	
	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}
	
	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);
		
		target.addAttribute("title", title);
		
		if (value != null)
			target.addAttribute("value", value);
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
			setValue(variables.get("value") + "xxx");
		
		requestRepaint();
		

	}

}
