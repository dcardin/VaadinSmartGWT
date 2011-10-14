package org.vaadin.smartgwt;

import com.google.gwt.core.client.JavaScriptObject;

@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VTabSet.class)
public class TabSet extends Layout
{
	public void setTabs(Tab... tabs)
	{
		for (Tab tab : tabs)
			addTab(tab);
	}

	public void addTab(Tab tab)
	{
		tab.setTabSet(this);
		addComponent(tab);
	}

	public void setTabTitle(int tabIndex, String title)
	{}

	/**
	 * Changes the title of a tab
	 * 
	 * @param ID
	 *            the tab ID
	 * @param title
	 *            new title
	 */
	public void setTabTitle(String ID, String title)
	{}

	/**
	 * Changes the title of a tab
	 * 
	 * @param tab
	 *            the tab
	 * @param title
	 *            new title
	 */
	public void setTabTitle(Tab tab, String title)
	{}

}
