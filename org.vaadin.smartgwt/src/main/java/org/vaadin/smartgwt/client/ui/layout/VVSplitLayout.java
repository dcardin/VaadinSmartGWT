package org.vaadin.smartgwt.client.ui.layout;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.VLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VVSplitLayout extends VLayout implements Paintable
{
	private final VSplitLayoutHelper splitLayoutHelper;
	private final Element element = DOM.createDiv();

	public VVSplitLayout()
	{
		splitLayoutHelper = new VSplitLayoutHelper(this)
			{
				@Override
				protected int getVisibleDimension(Canvas canvas)
				{
					return canvas.getVisibleWidth();
				}

				@Override
				protected void setDimension(Canvas canvas, int dimension)
				{
					canvas.setWidth(dimension);
				}
			};
	}

	@Override
	public Element getElement()
	{
		return element;
	}

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		splitLayoutHelper.updateFromUIDL(uidl, client);
	}

	@Override
	protected void onDraw()
	{
		super.onDraw();
		splitLayoutHelper.updateChildrenSizes();
	}
}