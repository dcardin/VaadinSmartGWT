package org.vaadin.smartgwt.server.layout;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.vaadin.smartgwt.server.BaseWidget;
import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.data.DataSource;
import org.vaadin.smartgwt.server.util.SC;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * The Master Container is the top most container of SmartVaadin applications. Why is it needed?
 * There are objects that exist in a static-like context on the client. DataSources, SC etc.
 * These objects must have a server counterpart and it must be held somewhere.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.layout.VMasterContainer.class)
public class MasterContainer extends BaseWidget implements ComponentContainer
{
	private final SC sc = new SC();
	private final List<DataSource> dataSources = new ArrayList<DataSource>();
	private Canvas pane;
	private Window window;
	
	public MasterContainer()
	{
		sc.setParent(this);
		setAttribute("sc", sc);
	}
	
	public void addDataSource(DataSource dataSource)
	{
		if (!dataSources.contains(dataSource))
		{
			if (dataSource.getParent() instanceof ComponentContainer)
			{
				((ComponentContainer) dataSource.getParent()).removeComponent(dataSource);
			}

			dataSource.setParent(this);
			dataSources.add(dataSource);
		}
	}

	public Canvas getPane()
	{
		return pane;
	}

	public void setPane(Canvas pane)
	{
		this.pane = pane;
		pane.setParent(this);
	}

	public SC getSC()
	{
		return sc;
	}
	
	public void showWindow(Window window)
	{
		this.window = window;
		window.setParent(this);
		requestRepaint();
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		target.startTag("dataSources");
		for (DataSource dataSource : dataSources)
		{
			dataSource.paint(target);
		}
		target.endTag("dataSources");

		if (pane != null)
		{
			target.startTag("pane");
			pane.paint(target);
			target.endTag("pane");
		}

		if (window != null)
		{
			target.startTag("window");
			window.paint(target);
			target.endTag("window");
			window = null;
		}

		super.paintContent(target);
	}

	@Override
	public void addComponent(Component c)
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public void removeComponent(Component c)
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public void removeAllComponents()
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent)
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public Iterator<Component> getComponentIterator()
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public void requestRepaintAll()
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public void moveComponentsFrom(ComponentContainer source)
	{
		throw new UnsupportedOperationException("ComponentContainer implemented only for vaadin integration.");
	}

	@Override
	public void addListener(ComponentAttachListener listener)
	{
		// ComponentContainer implemented only for vaadin integration.
	}

	@Override
	public void removeListener(ComponentAttachListener listener)
	{
		// ComponentContainer implemented only for vaadin integration.
	}

	@Override
	public void addListener(ComponentDetachListener listener)
	{
		// ComponentContainer implemented only for vaadin integration.
	}

	@Override
	public void removeListener(ComponentDetachListener listener)
	{
		// ComponentContainer implemented only for vaadin integration.
	}
}
