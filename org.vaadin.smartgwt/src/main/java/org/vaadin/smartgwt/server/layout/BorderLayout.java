package org.vaadin.smartgwt.server.layout;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.vaadin.smartgwt.server.Canvas;
import org.vaadin.smartgwt.server.Label;

import com.vaadin.ui.Component;

public class BorderLayout extends VLayout
{
	private final HLayout centerLayout = new HLayout();
	private Canvas center = new NullComponent();
	private Canvas north = new NullComponent();
	private Canvas south = new NullComponent();
	private Canvas west = new NullComponent();
	private Canvas east = new NullComponent();

	public BorderLayout()
	{
		centerLayout.setHeight("*");
		centerLayout.addMember(west);
		centerLayout.addMember(center);
		centerLayout.addMember(east);
		super.addMember(north);
		super.addMember(centerLayout);
		super.addMember(south);
	}

	public Canvas getCenterComponent()
	{
		return center instanceof NullComponent ? null : center;
	}

	public void setCenterComponent(Canvas component)
	{
		if (component == null)
		{
			centerLayout.replaceMember(this.center, this.center = new NullComponent());
		}
		else
		{
			component.setWidth("100%");
			component.setHeight("100%");
			centerLayout.replaceMember(this.center, this.center = component);
		}
	}

	public Canvas getNorthComponent()
	{
		return north instanceof NullComponent ? null : north;
	}

	public void setNorthComponent(Canvas component)
	{
		if (component == null)
		{
			super.replaceMember(this.north, this.north = new NullComponent());
		}
		else
		{
			component.setWidth("100%");
			super.replaceMember(this.north, this.north = component);
		}
	}

	public Canvas getSouthComponent()
	{
		return south instanceof NullComponent ? null : south;
	}

	public void setSouthComponent(Canvas component)
	{
		if (component == null)
		{
			super.replaceMember(this.south, this.south = new NullComponent());
		}
		else
		{
			component.setWidth("100%");
			super.replaceMember(this.south, this.south = component);
		}
	}

	public Canvas getWestComponent()
	{
		return west instanceof NullComponent ? null : west;
	}

	public void setWestComponent(Canvas component)
	{
		if (component == null)
		{
			centerLayout.replaceMember(this.west, this.west = new NullComponent());
		}
		else
		{
			component.setHeight("100%");
			centerLayout.replaceMember(this.west, this.west = component);
		}
	}

	public Canvas getEastComponent()
	{
		return east instanceof NullComponent ? null : east;
	}

	public void setEastComponent(Canvas component)
	{
		if (component == null)
		{
			centerLayout.replaceMember(this.east, this.east = new NullComponent());
		}
		else
		{
			component.setHeight("100%");
			centerLayout.replaceMember(this.east, this.east = component);
		}
	}

	@Override
	public Boolean hasMember(Canvas canvas)
	{
		return super.hasMember(canvas) || centerLayout.hasMember(canvas);
	}

	@Override
	public Canvas[] getMembers()
	{
		return newMembersSet().toArray(new Canvas[0]);
	}

	@Override
	public Iterator<Component> getComponentIterator()
	{
		return newMembersSet().iterator();
	}

	@Override
	public void addMember(Canvas component)
	{
		throw newUnsupportedOperationException();
	}

	@Override
	public void addMember(Canvas component, int position)
	{
		throw newUnsupportedOperationException();
	}

	@Override
	public void removeMember(Canvas member)
	{
		throw newUnsupportedOperationException();
	}

	@Override
	public void setMembers(Canvas... newMembers)
	{
		throw newUnsupportedOperationException();
	}

	@Override
	public void setMembers(List<Canvas> members)
	{
		throw newUnsupportedOperationException();
	}

	@Override
	public void removeMemberAt(int pos)
	{
		throw newUnsupportedOperationException();
	}

	@Override
	public void replaceMember(Canvas oldComponent, Canvas newComponent)
	{
		throw newUnsupportedOperationException();
	}

	private Set<Component> newMembersSet()
	{
		final Set<Component> members = new HashSet<Component>();
		members.add(getNorthComponent());
		members.add(getSouthComponent());
		members.add(getWestComponent());
		members.add(getEastComponent());
		members.add(getCenterComponent());
		members.remove(null);
		return members;
	}

	private UnsupportedOperationException newUnsupportedOperationException()
	{
		return new UnsupportedOperationException("Member operation not supported in BorderLayout.  Use constrainted component getters and setters instead.");
	}

	private static class NullComponent extends Label
	{
		public NullComponent()
		{
			super("");
			setVisible(false);
		}
	}
}