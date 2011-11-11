package org.vaadin.smartgwt;

@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VLabel.class)
public class Label extends StatefulCanvas
{
	public Label(String contents)
	{
		super();
		setContents(contents);
	}

}
