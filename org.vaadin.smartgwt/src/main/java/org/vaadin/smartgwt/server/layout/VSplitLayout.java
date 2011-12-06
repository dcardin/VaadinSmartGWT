package org.vaadin.smartgwt.server.layout;

import org.vaadin.smartgwt.client.ui.layout.VVSplitLayout;
import org.vaadin.smartgwt.server.Canvas;

import com.vaadin.ui.ClientWidget;

@ClientWidget(value = VVSplitLayout.class)
public class VSplitLayout extends Layout
{
	public VSplitLayout()
	{
		this(true, true);
	}

	public VSplitLayout(boolean showResizeBar, boolean resizeWithParent)
	{
		setShowResizeBar(showResizeBar);
		setResizeWithParent(resizeWithParent);
		setProportionalLayout(true);
	}

	public void setTopCanvas(Canvas canvas)
	{
		setAttribute("top", canvas);
		addMember(canvas);
	}

	public void setBottomCanvas(Canvas canvas)
	{
		setAttribute("bottom", canvas);
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
