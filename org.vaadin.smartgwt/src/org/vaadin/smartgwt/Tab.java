package org.vaadin.smartgwt;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

/**
 * Server side component for the VTab widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VTab.class)
public class Tab extends Layout
{
	private TabSet tabSet;

	public TabSet getTabSet()
	{
		return tabSet;
	}

	public void setTabSet(TabSet tabSet)
	{
		this.tabSet = tabSet;
	}

	public Tab()
	{
		super();
	}

	public Tab(String title)
	{
		super();
		setTitle(title);
	}

	public Component getPane()
	{
		return components.get(0);
	}

	public void setPane(Component pane)
	{
		removeAllComponents();
		addComponent(pane);
	}

	public void setTitle(String title)
	{
		setAttribute("*title", title);
		// tabSet.setTabTitle(this, title);
		// }
	}

	/**
	 * Specifies the title of the this tab.
	 * 
	 * @return String
	 */
	public String getTitle()
	{
		// if (tabSet == null || !tabSet.isDrawn())
		// {
		return getAttributeAsString("title");
		// }
		// else
		// {
		// return tabSet.getTab(getID()).getAttributeAsString("title");
		// }
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		// TODO Auto-generated method stub
		super.paintContent(target);
	}
}
