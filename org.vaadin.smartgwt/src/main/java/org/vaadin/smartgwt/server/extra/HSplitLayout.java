package org.vaadin.smartgwt.server.extra;

import static org.vaadin.smartgwt.server.builder.HLayoutBuilder.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.builder.CanvasBuilder;
import org.vaadin.smartgwt.server.layout.HLayout;

import com.vaadin.ui.ClientWidget;

@ClientWidget(org.vaadin.smartgwt.client.extra.VHSplitLayout.class)
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

	private HLayout left;
	private HLayout right;

	public HSplitLayout() {
		//@formatter:off
		setMembers(
			left = buildHLayout()
				.setWidth("50%")
				.setHeight(1)
				.build(),
			right = buildHLayout()
				.setWidth("*")
				.setHeight(1)
				.build()
		);
		//@formatter:on	
	}

	public void setProportions(double proportion) {
		left.setWidth(((int) (proportion * 100)) + "%");
	}

	public Canvas getLeftMember() {
		return left.getMembers().length > 0 ? left.getMembers()[0] : null;
	}

	public void setLeftMember(Canvas member) {
		if (member != null) {
			member.setWidth("100%");
			left.setMembers(member);
		} else {
			left.setMembers();
		}
	}

	public Canvas getRightMember() {
		return right.getMembers().length > 0 ? right.getMembers()[0] : null;
	}

	public void setRightMember(Canvas member) {
		if (member != null) {
			member.setWidth("100%");
			right.setMembers(member);
		} else {
			right.setMembers();
		}
	}
}
