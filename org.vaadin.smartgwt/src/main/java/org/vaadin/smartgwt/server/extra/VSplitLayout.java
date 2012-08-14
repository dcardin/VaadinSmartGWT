package org.vaadin.smartgwt.server.extra;

import static org.vaadin.smartgwt.server.builder.VLayoutBuilder.*;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.builder.CanvasBuilder;
import org.vaadin.smartgwt.server.layout.VLayout;

import com.vaadin.ui.ClientWidget;

@ClientWidget(org.vaadin.smartgwt.client.extra.VVSplitLayout.class)
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

	private VLayout top;
	private VLayout bottom;

	public VSplitLayout() {
		//@formatter:off
		setMembers(
			top = buildVLayout()
				.setHeight("50%")
				.setWidth(1)
				.build(),
			bottom = buildVLayout()
				.setHeight("*")
				.setWidth(1)
				.build()
		);
		//@formatter:on	
	}

	public void setProportions(double proportion) {
		top.setHeight(((int) (proportion * 100)) + "%");
	}

	public Canvas getTopMember() {
		return top.getMembers().length > 0 ? top.getMembers()[0] : null;
	}

	public void setTopMember(Canvas member) {
		if (member != null) {
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
			member.setHeight("100%");
			bottom.setMembers(member);
		} else {
			bottom.setMembers();
		}
	}
}