package org.vaadin.smartgwt.server.layout;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.vaadin.smartgwt.server.BaseWidget;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.core.PaintablePropertyPainter;
import org.vaadin.smartgwt.server.core.Reference;
import org.vaadin.smartgwt.server.data.DataSource;
import org.vaadin.smartgwt.server.util.SC;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * The Master Container is the top most container of SmartVaadin applications. Why is it needed? There are objects that exist in a static-like context on the
 * client. DataSources, SC etc. These objects must have a server counterpart and it must be held somewhere.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.layout.VMasterContainer.class)
public class MasterContainer extends BaseWidget implements ComponentContainer
{
	private final SC sc = new SC();
	private final PaintablePropertyPainter paintablePropertyPainter = new PaintablePropertyPainter();
	private final Reference<List<DataSource>> dataSources;
	private final Reference<Canvas> pane;
	private final Reference<Window> window;

	public MasterContainer()
	{
		dataSources = paintablePropertyPainter.addListProperty("dataSources");
		pane = paintablePropertyPainter.addProperty("pane");
		window = paintablePropertyPainter.addProperty("window");
		sc.setParent(this);
		setAttribute("sc", sc);
	}

	public void addDataSource(DataSource dataSource)
	{
		if (!dataSources.value.contains(dataSource))
		{
			if (dataSource.getParent() instanceof ComponentContainer)
			{
				((ComponentContainer) dataSource.getParent()).removeComponent(dataSource);
			}

			dataSource.setParent(this);
			dataSources.value.add(dataSource);
		}
	}

	public Canvas getPane()
	{
		return pane.value;
	}

	public void setPane(Canvas pane)
	{
		this.pane.value = pane;
		pane.setParent(this);
	}

	public SC getSC()
	{
		return sc;
	}

	public void showWindow(Window window)
	{
		this.window.value = window;
		window.setParent(this);
		requestRepaint();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		paintablePropertyPainter.paintContent(target);
		window.value = null;
		super.paintContent(target);
	}

	@Override
	public void addComponent(Component c)
	{

	}

	@Override
	public void removeComponent(Component c)
	{

	}

	@Override
	public void removeAllComponents()
	{

	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent)
	{

	}

	@Override
	public Iterator<Component> getComponentIterator()
	{
		return Collections.<Component> emptyList().iterator();
	}

	@Override
	public void requestRepaintAll()
	{

	}

	@Override
	public void moveComponentsFrom(ComponentContainer source)
	{

	}

	@Override
	public void addListener(ComponentAttachListener listener)
	{

	}

	@Override
	public void removeListener(ComponentAttachListener listener)
	{

	}

	@Override
	public void addListener(ComponentDetachListener listener)
	{

	}

	@Override
	public void removeListener(ComponentDetachListener listener)
	{

	}
}
