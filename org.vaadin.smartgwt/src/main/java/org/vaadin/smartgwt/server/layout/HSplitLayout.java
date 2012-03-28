package org.vaadin.smartgwt.server.layout;

import static com.google.common.base.Preconditions.*;

import org.vaadin.smartgwt.client.ui.layout.VHSplitLayout;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Label;
import org.vaadin.smartgwt.server.core.PaintablePropertyPainter;
import org.vaadin.smartgwt.server.core.Reference;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.ClientWidget;

@ClientWidget(value=VHSplitLayout.class)
public class HSplitLayout extends Layout
{
	private final PaintablePropertyPainter propertyPainter = new PaintablePropertyPainter();
	private final Reference<Canvas> left;
	private final Reference<Canvas> right;
	private final String[] proportions = new String[2];
	private boolean showResizeBar;
	private boolean resizeWithParent;

	public HSplitLayout()
	{
		this(true, true);
	}
	
	public HSplitLayout(boolean showResizeBar, boolean resizeWithParent)
	{
		left = propertyPainter.addProperty("left");
		right = propertyPainter.addProperty("right");
		this.showResizeBar = showResizeBar;
		this.resizeWithParent = resizeWithParent;
		this.left.value = new NullMember();
		this.right.value = new NullMember();
		setLeftCanvas(new NullMember());
		setRightCanvas(new NullMember());
		setProportionalLayout(true);
	}
	
	public void setLeftCanvas(Canvas canvas)
	{
		left.value.setParent(null);
		left.value = canvas == null ? new NullMember() : canvas;
		left.value.setParent(this);
		left.value.setWidth(proportions[0]);

		if (left.value instanceof NullMember == false && right.value instanceof NullMember == false)
		{
			left.value.setShowResizeBar(showResizeBar);
		}
	}

	public void setRightCanvas(Canvas canvas)
	{
		right.value.setParent(null);
		right.value = canvas == null ? new NullMember() : canvas;
		right.value.setParent(this);
		right.value.setWidth(proportions[1]);

		if (left.value instanceof NullMember == false && right.value instanceof NullMember == false)
		{
			left.value.setShowResizeBar(showResizeBar);
		}
	}

	public void setProportionalLayout(boolean proportional)
	{
		setProportions(0.5d, 0.5d);
	}

	public void setProportions(double leftProportion, double rightProportion)
	{
		checkArgument(leftProportion >= 0.0d && leftProportion <= 1.0d, "top proportion must be between 0.0 and 1.0.");
		checkArgument(rightProportion >= 0.0d && rightProportion <= 1.0d, "bottom proportion must be between 0.0 and 1.0.");
		checkArgument(leftProportion + rightProportion == 1.0d, "sum of top and bottom proportion must be equal to 1.0.");

		proportions[0] = (leftProportion * 100) + "%";
		proportions[1] = (rightProportion * 100) + "%";
		left.value.setHeight(proportions[0]);
		right.value.setHeight(proportions[1]);
	}
	
	public void setShowResizeBar(boolean showResizeBar)
	{
		this.showResizeBar = showResizeBar;

		if (left.value instanceof NullMember == false && right.value instanceof NullMember == false)
		{
			left.value.setShowResizeBar(showResizeBar);
		}
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
