package org.vaadin.smartgwt.client.extra;

import org.vaadin.smartgwt.client.ui.layout.VHLayout;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VHSplitLayout extends VHLayout {
	private String pid;
	private String[] initialHeights;

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
		if (hasLeftMember() && hasRightMember()) {
			insureInitialHeights();

			final int maxHeight = Math.max(getLeftMember().getVisibleHeight(), getRightMember().getVisibleHeight());
			getLeftMember().setHeight(maxHeight);
			getRightMember().setHeight(maxHeight);
		} else if (initialHeights != null) {
			if (hasLeftMember()) {
				getLeftMember().setHeight(initialHeights[0]);
			}

			if (hasRightMember()) {
				getRightMember().setHeight(initialHeights[1]);
			}

			initialHeights = null;
		}
	}

	private void insureInitialHeights() {
		if (initialHeights == null) {
			initialHeights = new String[] { getLeftMember().getHeightAsString(), getRightMember().getHeightAsString() };
		} else {
			getLeftMember().setHeight(initialHeights[0]);
			getRightMember().setHeight(initialHeights[1]);
		}
	}

	private Canvas getLeftMember() {
		return ((Layout) getMember(0)).getMember(0);
	}

	private boolean hasLeftMember() {
		return getLeftMember() != null;
	}

	private Canvas getRightMember() {
		return ((Layout) getMember(1)).getMember(0);
	}

	private boolean hasRightMember() {
		return getRightMember() != null;
	}
}
