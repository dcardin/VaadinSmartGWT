package org.vaadin.smartgwt.server.layout;

import static com.google.common.base.Preconditions.*;

import org.vaadin.smartgwt.client.ui.layout.VVSplitLayout;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Label;
import org.vaadin.smartgwt.server.core.PaintablePropertyPainter;
import org.vaadin.smartgwt.server.core.Reference;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(value = VVSplitLayout.class)
public class VSplitLayout extends Layout
{
	private final PaintablePropertyPainter propertyPainter = new PaintablePropertyPainter();
	private final Reference<Canvas> top;
	private final Reference<Canvas> bottom;
	private final String[] proportions = new String[2];
	private boolean showResizeBar;
	private boolean resizeWithParent;

	public VSplitLayout()
	{
		this(true, true);
	}

	public VSplitLayout(boolean showResizeBar, boolean resizeWithParent)
	{
		top = propertyPainter.addProperty("top");
		bottom = propertyPainter.addProperty("bottom");
		setShowResizeBar(showResizeBar);
		setResizeWithParent(resizeWithParent);
		setTopCanvas(top.value = new NullMember());
		setBottomCanvas(bottom.value = new NullMember());
		setProportionalLayout(true);
	}

	public void setTopCanvas(Canvas canvas)
	{
		top.value.setParent(null);
		top.value = canvas == null ? new NullMember() : canvas;
		top.value.setParent(this);
		top.value.setHeight(proportions[0]);
		top.value.setShowResizeBar(showResizeBar);
	}

	public void setBottomCanvas(Canvas canvas)
	{
		bottom.value.setParent(null);
		bottom.value = canvas == null ? new NullMember() : canvas;
		bottom.value.setParent(this);
		bottom.value.setHeight(proportions[1]);
	}

	public void setProportionalLayout(boolean proportional)
	{
		setProportions(0.5d, 0.5d);
	}

	public void setProportions(double topProportion, double bottomProportion)
	{
		checkArgument(topProportion >= 0.0d && topProportion <= 1.0d, "top proportion must be between 0.0 and 1.0.");
		checkArgument(bottomProportion >= 0.0d && bottomProportion <= 1.0d, "bottom proportion must be between 0.0 and 1.0.");
		checkArgument(topProportion + bottomProportion == 1.0d, "sum of top and bottom proportion must be equal to 1.0.");

		proportions[0] = (topProportion * 100) + "%";
		proportions[1] = (bottomProportion * 100) + "%";
		top.value.setHeight(proportions[0]);
		bottom.value.setHeight(proportions[1]);
	}

	public void setShowResizeBar(boolean showResizeBar)
	{
		this.showResizeBar = showResizeBar;
	}

	public void setResizeWithParent(boolean resizeWithParent)
	{
		this.resizeWithParent = resizeWithParent;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		target.addAttribute("*resizeWithParent", resizeWithParent);
		propertyPainter.paintContent(target);
		super.paintContent(target);
	}

	private static class NullMember extends Label
	{
		public NullMember()
		{
			super("");
			setVisible(false);
		}
	}
}
