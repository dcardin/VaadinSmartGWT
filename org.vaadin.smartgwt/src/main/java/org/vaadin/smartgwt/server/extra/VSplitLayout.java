package org.vaadin.smartgwt.server.extra;

import static org.vaadin.smartgwt.server.builder.LabelBuilder.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.builder.CanvasBuilder;
import org.vaadin.smartgwt.server.layout.VLayout;

public class VSplitLayout extends VLayout {
	public static class Builder extends CanvasBuilder<VSplitLayout, Builder> {
		Builder(VSplitLayout instance) {
			super(instance);
		}

		public Builder setProportions(double proportion) {
			instance().setProportions(proportion);
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

	private Canvas top = null;
	private Canvas bottom = null;

	public VSplitLayout() {
		//@formatter:off
		setMembers(
			top = buildLabel("")
				.setHeight("50%")
				.setWidth("100%")
				.build(),
			bottom = buildLabel("")
				.setHeight("*")
				.setWidth("100%")
				.build()
		);
		// @formatter:on	
	}

	public void setProportions(double proportion) {
		top.setHeight(((int) (proportion * 100)) + "%");
	}

	public Canvas getTopMember() {
		return top;
	}

	public void setTopMember(Canvas member) {
		String height = top.getHeightAsString();
		
		if (member == null) {
			top = buildLabel("")
					.setHeight("50%")
					.setWidth("100%")
					.build();
		}
		else {
			top = member;
			top.setHeight(height);
		}
		setMembers(top,bottom);
	}

	public Canvas getBottomMember() {
		return bottom;
	}

	public void setBottomMember(Canvas member) {
		String height = bottom.getHeightAsString();
		
		if (member == null) {
			bottom = buildLabel("")
					.setHeight("*")
					.setWidth("100%")
					.build();
		}
		else {
			bottom = member;
			bottom.setHeight(height);
		}
		
		setMembers(top,bottom);
	}
}