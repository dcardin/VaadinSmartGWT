package org.vaadin.smartgwt.server.layout;

import static com.google.common.base.Preconditions.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Label;
import org.vaadin.smartgwt.server.core.ComponentPropertyPainter;
import org.vaadin.smartgwt.server.core.ComponentReference;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public abstract class AbstractSplitLayout extends Canvas {
	private final ComponentPropertyPainter propertyPainter = new ComponentPropertyPainter(this);
	private final ComponentReference<Canvas> member1 = propertyPainter.addProperty("member1");
	private final ComponentReference<Canvas> member2 = propertyPainter.addProperty("member2");
	private final String[] proportions = new String[2];
	private boolean showResizeBar;
	private boolean resizeWithParent;

	public AbstractSplitLayout() {
		this(true, true);
	}

	public AbstractSplitLayout(boolean showResizeBar, boolean resizeWithParent) {
		this.showResizeBar = showResizeBar;
		this.resizeWithParent = resizeWithParent;
		member1.set(new NullMember());
		member2.set(new NullMember());
		setMember1(new NullMember());
		setMember2(new NullMember());
		setProportionalLayout(true);
	}

	public void setProportionalLayout(boolean proportional) {
		setProportions(0.5d, 0.5d);
	}

	public void setProportions(double topProportion, double bottomProportion) {
		checkArgument(topProportion >= 0.0d && topProportion <= 1.0d, "top proportion must be between 0.0 and 1.0.");
		checkArgument(bottomProportion >= 0.0d && bottomProportion <= 1.0d, "bottom proportion must be between 0.0 and 1.0.");
		checkArgument(topProportion + bottomProportion == 1.0d, "sum of top and bottom proportion must be equal to 1.0.");

		proportions[0] = (topProportion * 100) + "%";
		proportions[1] = (bottomProportion * 100) + "%";
		setMemberProportion(member1.get(), proportions[0]);
		setMemberProportion(member2.get(), proportions[1]);
	}

	public void setShowResizeBar(boolean showResizeBar) {
		this.showResizeBar = showResizeBar;

		if (member1.get() instanceof NullMember == false && member2.get() instanceof NullMember == false) {
			member1.get().setShowResizeBar(showResizeBar);
		}
	}

	public void setResizeWithParent(boolean resizeWithParent) {
		this.resizeWithParent = resizeWithParent;
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		target.addAttribute("*resizeWithParent", resizeWithParent);
		propertyPainter.paintContent(target);
		super.paintContent(target);
	}

	protected Canvas getMember1() {
		return member1.get();
	}

	protected void setMember1(Canvas canvas) {
		member1.get().setParent(null);
		member1.set(canvas == null ? new NullMember() : canvas);
		member1.get().setParent(this);
		setMemberProportion(member1.get(), proportions[0]);

		if (member1.get() instanceof NullMember == false && member2.get() instanceof NullMember == false) {
			member1.get().setShowResizeBar(showResizeBar);
		}
	}

	protected Canvas getMember2() {
		return member2.get();
	}

	protected void setMember2(Canvas canvas) {
		member2.get().setParent(null);
		member2.set(canvas == null ? new NullMember() : canvas);
		member2.get().setParent(this);
		setMemberProportion(member2.get(), proportions[1]);

		if (member1.get() instanceof NullMember == false && member2.get() instanceof NullMember == false) {
			member1.get().setShowResizeBar(showResizeBar);
		}
	}

	protected abstract void setMemberProportion(Canvas member, String proportion);

	private static class NullMember extends Label {
		public NullMember() {
			super("");
			setVisible(false);
		}
	}
}
