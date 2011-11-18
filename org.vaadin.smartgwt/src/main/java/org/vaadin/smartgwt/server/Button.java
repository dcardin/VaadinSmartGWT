package org.vaadin.smartgwt.server;

import java.lang.reflect.Method;
import java.util.Map;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

/**
 * Server side component for the VIButton widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VButton.class)
public class Button extends StatefulCanvas
{
	private static final Method BUTTON_CLICK_METHOD;
	int i = 0;

	static
	{
		try
		{
			BUTTON_CLICK_METHOD = ClickListener.class.getDeclaredMethod("buttonClick", new Class[] { ClickEvent.class });
		}
		catch (final java.lang.NoSuchMethodException e)
		{
			// This should never happen
			throw new java.lang.RuntimeException("Internal error finding methods in Button");
		}
	}

	public Button()
	{
		super();
	}

	public Button(String title)
	{
		super();
		setTitle(title);
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
	}

}
