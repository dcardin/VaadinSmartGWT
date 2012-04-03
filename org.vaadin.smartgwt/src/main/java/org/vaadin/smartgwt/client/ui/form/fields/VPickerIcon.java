package org.vaadin.smartgwt.client.ui.form.fields;

import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;

public class VPickerIcon extends VFormItemIcon
{
	private static String IMG_TYPE = getImgType();

	// @formatter: off
    private static native String getImgType() /*-{
        var imgType = $wnd.isc.pickerImgType;
        return imgType == null || imgType === undefined ? "png" : imgType;
    }-*/;
	// @formatter: on
	
	@Override
	protected void postAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		super.postAttributeUpdateFromUIDL(uidl, client);

		final String src = uidl.getStringAttribute("src").substring(1);
		if (src.endsWith("$IMG_TYPE"))
		{
			getJSObject().setSrc(src.replace("$IMG_TYPE", IMG_TYPE));
		}
	}
}
