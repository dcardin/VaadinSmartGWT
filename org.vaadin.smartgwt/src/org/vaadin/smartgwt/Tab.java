package org.vaadin.smartgwt;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;

/**
 * Server side component for the VTab widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VTab.class)
public class Tab extends Container
{
	private String title;

	public Tab()
	{
		super();
	}

	public Tab(String title)
	{
		super();
		setTitle(title);
	}

	public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
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

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);

		if (title != null)
			target.addAttribute("title", title);
	}
}
