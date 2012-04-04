package org.vaadin.smartgwt.server.layout;

import org.vaadin.smartgwt.client.ui.layout.VVSplitLayout;
import org.vaadin.smartgwt.server.Canvas;

import com.vaadin.ui.ClientWidget;

@ClientWidget(value = VVSplitLayout.class)
public class VSplitLayout extends AbstractSplitLayout
{
	public VSplitLayout()
	{
		super();
		setHeight100();
	}

	public VSplitLayout(boolean showResizeBar, boolean resizeWithParent)
	{
		super(showResizeBar, resizeWithParent);
		setHeight100();
	}

	public void setTopCanvas(Canvas canvas)
	{
		setMember1(canvas);
	}

	public void setBottomCanvas(Canvas canvas)
	{
		setMember2(canvas);
	}

	@Override
	protected void setMemberProportion(Canvas member, String proportion)
	{
		member.setHeight(proportion);
	}
}
