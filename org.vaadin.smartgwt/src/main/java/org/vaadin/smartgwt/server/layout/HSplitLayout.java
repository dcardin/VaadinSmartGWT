package org.vaadin.smartgwt.server.layout;

import org.vaadin.smartgwt.client.ui.layout.VHSplitLayout;
import org.vaadin.smartgwt.server.Canvas;

import com.vaadin.ui.ClientWidget;

@ClientWidget(value=VHSplitLayout.class)
public class HSplitLayout extends Layout
{
	public HSplitLayout()
	{
		this(true, true);
	}
	
	public HSplitLayout(boolean showResizeBar, boolean resizeWithParent)
	{
		setShowResizeBar(showResizeBar);
		setResizeWithParent(resizeWithParent);
		setProportionalLayout(true);
	}
	
	public void setLeftCanvas(Canvas canvas)
	{
		setAttribute("left", canvas);
		addMember(canvas);
	}

	public void setRightCanvas(Canvas canvas)
	{
		setAttribute("right", canvas);
		addMember(canvas);
	}

	public void setProportionalLayout(boolean proportional)
	{
		setProportions(0.5d, 0.5d);
	}

	public void setProportions(double... proportions)
	{
		if (proportions.length != 2)
			throw new IllegalArgumentException("Need two proportions exactly");
		
		String[] result = new String[proportions.length];
		
		for (int i=0; i < proportions.length; i++)
		{
			Integer value = ((int) (proportions[i]*100));
			
			result[i] = value.toString() + "%";
		}
		
		setAttribute("*proportions", result);
	}
	
	public void setShowResizeBar(boolean showResizeBar)
	{
		setAttribute("*showResizeBar", showResizeBar);
	}
	
	public void setResizeWithParent(boolean resizeWithParent)
	{
		setAttribute("resizeWithParent", resizeWithParent);
	}
}
