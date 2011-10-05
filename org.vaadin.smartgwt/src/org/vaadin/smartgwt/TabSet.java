package org.vaadin.smartgwt;

@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VTabSet.class)
public class TabSet extends Container
{
	public void setTabs(Tab... tabs)
	{
		for (Tab tab : tabs)
			addComponent(tab);
	}
}
