package org.vaadin.smartgwt.server.extra;

import static org.vaadin.smartgwt.server.builder.VLayoutBuilder.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.builder.CanvasBuilder;
import org.vaadin.smartgwt.server.layout.VLayout;
import org.vaadin.smartgwt.server.types.Overflow;

public class VSplitLayout extends VLayout {
	public static class Builder extends CanvasBuilder<VSplitLayout, Builder> {
		Builder(VSplitLayout instance) {
			super(instance);
		}

		public Builder setProportions(double proportion) {
			instance().setProportions(proportion);
			return me();
		}

		public Builder setResizeBarVisible(boolean showResizeBar) {
			instance().setResizeBarVisible(showResizeBar);
			return me();
		}

		public Builder setTopMember(Canvas member) {
			instance().setTopMember(member);
			return me();
		}

		public Builder setBottomMember(Canvas member) {
			instance().setBottomMember(member);
			return me();
		}

		@Override
		protected Builder me() {
			return this;
		}
	}

	public static Builder buildVSplitLayout() {
		return new Builder(new VSplitLayout());
	}

	private VLayout top;
	private VLayout bottom;

	public VSplitLayout() {
		//@formatter:off
		setMembers(
			top = buildVLayout()
				.setWidth("100%")
				.setHeight("50%")
				.setOverflow(Overflow.AUTO)
				.setResizeBarVisible(true)
				.setResizeBarTarget("next")
				.build(),
			bottom = buildVLayout()
				.setWidth("100%")
				.setHeight("*")
				.setOverflow(Overflow.AUTO)
				.build()
		);
		//@formatter:on	
	}

	public void setProportions(double proportion) {
		top.setHeight(((int) (proportion * 100)) + "%");
	}

	public boolean isResizeBarVisible() {
		return top.getShowResizeBar();
	}

	public void setResizeBarVisible(boolean showResizeBar) {
		top.setShowResizeBar(showResizeBar);
	}

	public Canvas getTopMember() {
		return top.getMembers().length > 0 ? top.getMembers()[0] : null;
	}

	public void setTopMember(Canvas member) {
		if (member != null) {
			member.setWidth("100%");
			member.setHeight("100%");
			top.setMembers(member);
		} else {
			top.setMembers();
		}
	}

	public Canvas getBottomMember() {
		return bottom.getMembers().length > 0 ? bottom.getMembers()[0] : null;
	}

	public void setBottomMember(Canvas member) {
		if (member != null) {
			member.setWidth("100%");
			member.setHeight("100%");
			bottom.setMembers(member);
		} else {
			bottom.setMembers();
		}
	}
}