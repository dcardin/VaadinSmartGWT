package org.vaadin.smartgwt.client.ui.layout;

import org.vaadin.smartgwt.client.core.PaintableProperty;
import org.vaadin.smartgwt.client.core.PaintablePropertyUpdater;
import org.vaadin.smartgwt.client.core.VDataClass;
import org.vaadin.smartgwt.client.core.VJSObject;
import org.vaadin.smartgwt.client.ui.utils.PainterHelper;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VSectionStackSection extends VDataClass<SectionStackSection>
{
	private final PaintablePropertyUpdater propertyUpdater = new PaintablePropertyUpdater();

	public VSectionStackSection()
	{
		super(new SectionStackSection());
		
		propertyUpdater.addProperty(new PaintableProperty("items")
			{
				@Override
				public void postUpdate(Paintable[] paintables)
				{
					for (Paintable paintable : paintables)
					{
						if (paintable instanceof Canvas)
						{
							getJSObject().addItem((Canvas) paintable);
						}
						else if (paintable instanceof VJSObject)
						{
							final Object object = ((VJSObject<?>) paintable).getJSObject();

							if (object instanceof Canvas)
							{
								getJSObject().addItem((Canvas) object);
							}
						}
					}
				}
			});
	}

	@Override
	protected void updateJSObjectAttributes(UIDL uidl)
	{
		PainterHelper.updateDataObject(getClient(), getJSObject(), uidl);
	}

	@Override
	protected void updateFromUIDL(UIDL uidl)
	{
		propertyUpdater.updateFromUIDL(uidl, getClient());
	}
}