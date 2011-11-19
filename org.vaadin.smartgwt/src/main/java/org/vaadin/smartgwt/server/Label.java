package org.vaadin.smartgwt.server;

@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VLabel.class)
public class Label extends StatefulCanvas
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Label(String contents)
	{
		super();
		setContents(contents);
	}
}
