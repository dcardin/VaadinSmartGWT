package org.vaadin.smartgwt.server;

import org.vaadin.smartgwt.client.ui.VRadioGroupItem;

/**
 * Simple button subclass of StretchImgButton with appearance appropriate for a ToolStrip button.
 * Can be used to create an icon-only button, and icon with text, or a text only button by setting the
 * icon and title attributes as required.
 *
 */
@com.vaadin.ui.ClientWidget(VRadioGroupItem.class)
public class RadioGroupItem extends StretchImgButton {

	public RadioGroupItem() {
		scClassName = "RadioGroupItem";
	}

	/**
	 * Constructor.
	 * @param title title of the button
	 */
	public RadioGroupItem(String title) {
		this();
		setTitle(title);
	}

	/**
	 * Constructor.
	 * @param title title of the button
	 * @param icon the button icon
	 */
	public RadioGroupItem(String title, String group) {
		this();
		setTitle(title);
		setRadioGroup(group);
	}
}
