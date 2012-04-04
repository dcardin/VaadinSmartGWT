package org.vaadin.smartgwt.client.ui.layout;


import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.HLayout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VHSplitLayout extends HLayout implements Paintable
{
	private final VSplitLayoutHelper splitLayoutHelper;
	private final Element element = DOM.createDiv();

	public VHSplitLayout()
	{
		splitLayoutHelper = new VSplitLayoutHelper(this)
			{
				@Override
				protected int getVisibleDimension(Canvas canvas)
				{
					return canvas.getVisibleHeight();
				}

				@Override
				protected void setDimension(Canvas canvas, int dimension)
				{
					canvas.setHeight(dimension);
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