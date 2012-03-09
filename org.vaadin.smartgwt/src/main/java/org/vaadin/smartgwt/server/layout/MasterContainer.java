package org.vaadin.smartgwt.server.layout;

import java.util.Iterator;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Window;
import org.vaadin.smartgwt.server.data.DataSource;
import org.vaadin.smartgwt.server.util.SC;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * The Master Container is the top most container of SmartVaadin applications. Why is it needed?
 * There are objects that exist in a static-like context on the client. DataSources, SC etc.
 * These objects must have a server counterpart and it must be held somewhere.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.layout.VMasterContainer.class)
public class MasterContainer extends Layout implements ComponentContainer
{
	private static final long serialVersionUID = 1L;
	private SC sc = new SC();
	
	public void registerDataSource(DataSource dataSource)
	{
		if (dataSource.getParent() == null)
			dataSource.setParent(this);
		
		setAttribute("dataSource", dataSource);
	}
	
	public MasterContainer()
	{
		sc.setParent(this);
		setAttribute("sc", sc);
	}
	
	public SC getSC()
	{
		return sc;
	}
	
	public void showWindow(Window window)
	{
		window.setParent(this);
		setAttribute("*window", window);
		requestRepaint();
	}
	
	@Override
	public void addMember(Canvas c)
	{
		throw new java.lang.UnsupportedOperationException("Do not use use addMember on MasterContainer, use setPane() instead");
	}
	
	public void setPane(Canvas pane)
	{
		pane.setParent(this);
		setAttribute("*pane", pane);
	}
	
	@Override
	public void addComponent(Component c)
	{
		throw new java.lang.UnsupportedOperationException("Use setPane() instead");
	}

	@Override
	public void removeComponent(Component c)
	{
		throw new java.lang.UnsupportedOperationException("Use setPane() instead");
	}

	@Override
	public void removeAllComponents()
	{
		throw new java.lang.UnsupportedOperationException("Use setPane() instead");
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent)
	{
		throw new java.lang.UnsupportedOperationException("Use setPane() instead");
	}

	@Override
	public Iterator<Component> getComponentIterator()
	{
		throw new java.lang.UnsupportedOperationException("Use setPane() instead");
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
	{}

	@Override
	public void removeListener(ComponentAttachListener listener)
	{}

	@Override
	public void addListener(ComponentDetachListener listener)
	{}

	@Override
	public void removeListener(ComponentDetachListener listener)
	{}
}
