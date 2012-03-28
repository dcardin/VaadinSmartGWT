package org.vaadin.smartgwt.server.layout;

import static com.google.common.base.Preconditions.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Label;
import org.vaadin.smartgwt.server.core.PaintablePropertyPainter;
import org.vaadin.smartgwt.server.core.Reference;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public abstract class AbstractSplitLayout extends Canvas
{
	private final PaintablePropertyPainter propertyPainter = new PaintablePropertyPainter();
	private final Reference<Canvas> member1;
	private final Reference<Canvas> member2;
	private final String[] proportions = new String[2];
	private boolean showResizeBar;
	private boolean resizeWithParent;

	public AbstractSplitLayout()
	{
		this(true, true);
	}

	public AbstractSplitLayout(boolean showResizeBar, boolean resizeWithParent)
	{
		member1 = propertyPainter.addProperty("member1");
		member2 = propertyPainter.addProperty("member2");
		this.showResizeBar = showResizeBar;
		this.resizeWithParent = resizeWithParent;
		member1.value = new NullMember();
		member2.value = new NullMember();
		setMember1(new NullMember());
		setMember2(new NullMember());
		setProportionalLayout(true);
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
		setMemberProportion(member1.value, proportions[0]);
		setMemberProportion(member2.value, proportions[1]);
	}

	public void setShowResizeBar(boolean showResizeBar)
	{
		this.showResizeBar = showResizeBar;

		if (member1.value instanceof NullMember == false && member2.value instanceof NullMember == false)
		{
			member1.value.setShowResizeBar(showResizeBar);
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

	protected void setMember1(Canvas canvas)
	{
		member1.value.setParent(null);
		member1.value = canvas == null ? new NullMember() : canvas;
		member1.value.setParent(this);
		setMemberProportion(member1.value, proportions[0]);

		if (member1.value instanceof NullMember == false && member2.value instanceof NullMember == false)
		{
			member1.value.setShowResizeBar(showResizeBar);
		}
	}

	protected void setMember2(Canvas canvas)
	{
		member2.value.setParent(null);
		member2.value = canvas == null ? new NullMember() : canvas;
		member2.value.setParent(this);
		setMemberProportion(member2.value, proportions[1]);

		if (member1.value instanceof NullMember == false && member2.value instanceof NullMember == false)
		{
			member1.value.setShowResizeBar(showResizeBar);
		}
	}

	protected abstract void setMemberProportion(Canvas member, String proportion);

	private static class NullMember extends Label
	{
		public NullMember()
		{
			super("");
			setVisible(false);
		}
	}
}
