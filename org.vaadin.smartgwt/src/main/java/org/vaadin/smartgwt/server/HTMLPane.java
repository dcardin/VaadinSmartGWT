/*
 * Smart GWT (GWT for SmartClient)
 * Copyright 2008 and beyond, Isomorphic Software, Inc.
 *
 * Smart GWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  Smart GWT is also
 * available under typical commercial license terms - see
 * http://smartclient.com/license
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package org.vaadin.smartgwt.server;

import org.vaadin.smartgwt.server.types.ContentsType;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.ClientWidget;

/**
 * Use the HTMLPane component to display HTML content in a pane of specified size. If the HTML content is larger than the
 * size of the pane, the pane will provide scrollbars for viewing clipped content. <P> You can set the size of an HTMLPane
 * directly via the width and height properties, or indirectly by placing the HTMLPane in a container component ({@link
 * com.smartgwt.client.widgets.layout.Layout}, {@link com.smartgwt.client.widgets.Window},  {@link
 * com.smartgwt.client.widgets.layout.SectionStack}, etc) that manages the sizes of its members.
 */
@ClientWidget(org.vaadin.smartgwt.client.ui.VHTMLPane.class)
public class HTMLPane extends HTMLFlow {
	public HTMLPane() {
		scClassName = "HTMLPane";
	}

	/**
	 * Set the url of the IFrame.
	 *
	 * @param url the IFrame url
	 */
	public void setIFrameURL(String url) {
		setContentsURL(url);
		if (!isDrawn()) {
			setContentsType(ContentsType.PAGE);
		} else {
			assert getContentsType() == ContentsType.PAGE : "This method cannot be called on a HTMLPane that has a contentsType other than PAGE";
		}
	}

	/**
	 * The Vaadin resource used as content for the IFrame. 
	 */
	public void setIFrameResource(Resource resource) {
		setContentsResource(resource);
		if (!isDrawn()) {
			setContentsType(ContentsType.PAGE);
		} else {
			assert getContentsType() == ContentsType.PAGE : "This method cannot be called on a HTMLPane that has a contentsType other than PAGE";
		}
	}
}
