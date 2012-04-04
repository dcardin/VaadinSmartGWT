package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableListListener;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VDataClass;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSectionStackSection extends VDataClass<SectionStackSection>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VSectionStackSection()
	{
		super(new SectionStackSection());
		
		propertyUpdater.addPaintableListListener("items", new PaintableListListener()
			{
				@Override
				public void onAdd(Paintable[] source, Integer index, Paintable element)
				{
					getJSObject().addItem((Canvas) element);
				}

				@Override
				public void onRemove(Paintable[] source, Integer index, Paintable element)
				{

				}
			});
	}

	@Override
	protected void preAttributeUpdateFromUIDL(UIDL uidl, ApplicationConnection client)
	{
		propertyUpdater.updateFromUIDL(uidl, client);
	}
}