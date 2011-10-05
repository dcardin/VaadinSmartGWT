package org.vaadin.smartgwt;

import com.vaadin.ui.Component;

public class BorderLayout extends VLayout
{
	protected Component north = new Label("");
	protected Component west = new Label("");
	protected Component center = new Label("");
	protected Component east = new Label("");
	protected Component south = new Label("");
	private HLayout centerLayout;
	
	public BorderLayout()
	{
		centerLayout= new HLayout();
		centerLayout.addComponent(west);
		centerLayout.addComponent(center);
		centerLayout.addComponent(east);
		centerLayout.setSizeFull();
		
		addComponent(north);
		addComponent(centerLayout);
		addComponent(south);
		setWidth("100%");
		setHeight("100%");
	}

}
