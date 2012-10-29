package org.vaadin.smartgwt.server.extra;

import static org.vaadin.smartgwt.server.builder.LabelBuilder.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.builder.CanvasBuilder;
import org.vaadin.smartgwt.server.layout.HLayout;

public class HSplitLayout extends HLayout {
	public static class Builder extends CanvasBuilder<HSplitLayout, Builder> {
		Builder(HSplitLayout instance) {
			super(instance);
		}

		public Builder setProportions(double proportion) {
			instance().setProportions(proportion);
			return me();
		}

		public Builder setLeftMember(Canvas member) {
			instance().setLeftMember(member);
			return me();
		}

		public Builder setRightMember(Canvas member) {
			instance().setRightMember(member);
			return me();
		}

		@Override
		protected Builder me() {
			return this;
		}
	}

	public static Builder buildHSplitLayout() {
		return new Builder(new HSplitLayout());
	}

	private Canvas left = null;
	private Canvas right = null;

	public HSplitLayout() {
		//@formatter:off
		setMembers(
			left = buildLabel("")
				.setWidth("50%")
				.setHeight("100%")
				.build(),
			right = buildLabel("")
				.setWidth("*")
				.setHeight("100%")
				.build()
		);
		// @formatter:on	
	}

	public void setProportions(double proportion) {
		left.setHeight(((int) (proportion * 100)) + "%");
	}

	public Canvas getLeftMember() {
		return left;
	}

	public void setLeftMember(Canvas member) {
		String width = left.getWidthAsString();
		
		if (member == null) {
			left = buildLabel("")
					.setWidth("50%")
					.setHeight("100%")
					.build();
		}
		else {
			left = member;
			left.setWidth(width);
		}
		setMembers(left,right);
	}

	public Canvas getRightMember() {
		return right;
	}

	public void setRightMember(Canvas member) {
		String width = right.getWidthAsString();

		if (member == null) {
			right = buildLabel("")
					.setWidth("*")
					.setHeight("100%")
					.build();
		}
		else {
			right = member;
			right.setWidth(width);
		}
		
		setMembers(left,right);
	}
}
