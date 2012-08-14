package org.vaadin.smartgwt.client.extra;

import org.vaadin.smartgwt.client.ui.layout.VVLayout;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VVSplitLayout extends VVLayout {
	private String pid;
	private String[] initialWidths;

	@Override
	public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
		super.updateFromUIDL(uidl, client);

		if (pid == null) {
			pid = uidl.getId();

			addDrawHandler(new DrawHandler() {
				@Override
				public void onDraw(DrawEvent event) {
					resizeMembers();
				}
			});

			addResizedHandler(new ResizedHandler() {
				@Override
				public void onResized(ResizedEvent event) {
					resizeMembers();
				}
			});
		}
	}

	private void resizeMembers() {
		if (hasTopMember() && hasBottomMember()) {
			insureInitialWidths();

			final int maxWidth = Math.max(getTopMember().getVisibleWidth(), getBottomMember().getVisibleWidth());
			getTopMember().setWidth(maxWidth);
			getBottomMember().setWidth(maxWidth);
		} else if (initialWidths != null) {
			if (hasTopMember()) {
				getTopMember().setWidth(initialWidths[0]);
			}

			if (hasBottomMember()) {
				getBottomMember().setWidth(initialWidths[1]);
			}

			initialWidths = null;
		}
	}

	private void insureInitialWidths() {
		if (initialWidths == null) {
			initialWidths = new String[] { getTopMember().getWidthAsString(), getBottomMember().getWidthAsString() };
		} else {
			getTopMember().setWidth(initialWidths[0]);
			getBottomMember().setWidth(initialWidths[1]);
		}
	}

	private Canvas getTopMember() {
		return ((Layout) getMember(0)).getMember(0);
	}

	private boolean hasTopMember() {
		return getTopMember() != null;
	}

	private Canvas getBottomMember() {
		return ((Layout) getMember(1)).getMember(0);
	}

	private boolean hasBottomMember() {
		return getBottomMember() != null;
	}
}
