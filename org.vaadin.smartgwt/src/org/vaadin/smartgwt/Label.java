package org.vaadin.smartgwt;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.VLabel.class)
public class Label extends AbstractComponent
{
	private String contents;

	public Label(String contents)
	{
		super();
		setContents(contents);
	}

	public String getContents()
	{
		return contents;
	}

	public void setContents(String contents)
	{
		this.contents = contents;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		super.paintContent(target);
	}

}
