package org.vaadin.smartgwt.client;

import java.util.Date;

import org.vaadin.smartgwt.client.ui.layout.VVLayout;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ResizedEvent;
import com.smartgwt.client.widgets.events.ResizedHandler;

public class VRendererPanel extends VVLayout
{
	private Img main;

	public VRendererPanel()
	{
		setHeight100();
		setWidth100();
		main = new Img();
		main.setHeight("100%");
		main.setWidth("100%");

		addMember(main);

		main.addResizedHandler(new ResizedHandler()
			{
				@Override
				public void onResized(ResizedEvent event)
				{
					updateImage();
				}
			});
	}

	public void updateImage()
	{
		String width = main.getWidthAsString();
		String height = main.getHeightAsString();

		DateTimeFormat format = DateTimeFormat.getFormat("MMddyyyyHHmmssS");
		String date = format.format(new Date());

		main.setSrc(GWT.getHostPageBaseURL() + "imageFetcher?type=main&width=" + width + "&height=" + height + "&time=" + date);
	}
}
