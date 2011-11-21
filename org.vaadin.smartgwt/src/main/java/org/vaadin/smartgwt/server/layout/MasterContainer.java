package org.vaadin.smartgwt.server.layout;

import java.util.Iterator;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.data.DataSource;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;

/**
 * Server side component for the VVLayout widget.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.ui.layout.VMasterContainer.class)
public class MasterContainer extends Layout implements ComponentContainer
{
	public static MasterContainer instance;
	
	public static MasterContainer getInstance()
	{
		if (instance == null)
			instance = new MasterContainer();
		
		return instance;
	}
	
	@Override
	public void paintContent(PaintTarget target) throws PaintException
	{
		// TODO Auto-generated method stub
		super.paintContent(target);
	}
	
	public MasterContainer()
	{
		MasterContainer.instance = this;
	}
	
	private static final long serialVersionUID = 1L;

	public static void addGlobalDatasource(DataSource dataSource)
	{
		if (dataSource.getParent() == null)
			dataSource.setParent(getInstance());
		
		getInstance().setAttribute("dataSource", dataSource);
	}
	
	@Override
	public void addMember(Canvas c)
	{
		throw new java.lang.UnsupportedOperationException("Do not use use addMember on MasterContainer, use setPane() instead");
	}
	
	public void setPane(Canvas pane)
	{
		pane.setParent(this);
		setAttribute("pane", pane);
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
		// TODO Auto-generated method stub
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
