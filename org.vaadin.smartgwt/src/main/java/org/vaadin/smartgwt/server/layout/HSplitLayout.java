package org.vaadin.smartgwt.server.layout;

import org.vaadin.smartgwt.client.ui.layout.VHSplitLayout;
import org.vaadin.smartgwt.server.Canvas;

import com.vaadin.ui.ClientWidget;

@ClientWidget(value = VHSplitLayout.class)
public class HSplitLayout extends AbstractSplitLayout {
	public HSplitLayout() {
		super();
		setWidth100();
	}

	public HSplitLayout(boolean showResizeBar, boolean resizeWithParent) {
		super(showResizeBar, resizeWithParent);
		setWidth100();
	}

	public Canvas getLeftCanvas() {
		return getMember1();
	}

	public void setLeftCanvas(Canvas canvas) {
		setMember1(canvas);
	}

	public Canvas getRightCanvas() {
		return getMember2();
	}

	public void setRightCanvas(Canvas canvas) {
		setMember2(canvas);
	}

	@Override
	protected void setMemberProportion(Canvas member, String proportion) {
		member.setWidth(proportion);
	}
}
