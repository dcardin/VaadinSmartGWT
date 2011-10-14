package org.vaadin.smartgwt;

import java.util.Iterator;

import com.smartgwt.client.types.LayoutResizeBarPolicy;
import com.vaadin.ui.Component;

public class BorderLayout extends VLayout
{
	public enum Constraint
	{
		NORTH, WEST, CENTER, EAST, SOUTH;
	}

	public static final String DEFAULT_MINIMUM_HEIGHT = "50px";

	private HLayout centerLayout;

	private String minimumNorthHeight = DEFAULT_MINIMUM_HEIGHT;
	private String minimumSouthHeight = DEFAULT_MINIMUM_HEIGHT;
	private String minimumWestWidth = DEFAULT_MINIMUM_HEIGHT;
	private String minimumEastWidth = DEFAULT_MINIMUM_HEIGHT;

	protected Component north = new Label("");
	protected Component west = new Label("");
	protected Component center = new Label("");
	protected Component east = new Label("");
	protected Component south = new Label("");

	/**
	 * Create a layout structure that mimics the traditional {@link java.awt.BorderLayout}.
	 */
	public BorderLayout()
	{
		((Label) north).setBackgroundColor("red");
		((Label) south).setBackgroundColor("red");
		((Label) west).setBackgroundColor("blue");
		((Label) east).setBackgroundColor("blue");
		((Label) center).setBackgroundColor("green");
		
		setDefaultResizeBars(LayoutResizeBarPolicy.MARKED);
		((Label) north).setShowResizeBar(true);
		((Label) west).setShowResizeBar(true);
		((Label) center).setShowResizeBar(true);
		((Label) center).setResizeBarTarget("next");
		
		centerLayout = new HLayout();
		centerLayout.setDefaultResizeBars(LayoutResizeBarPolicy.MARKED);
		centerLayout.setSizeFull();
		centerLayout.setShowResizeBar(true);
		centerLayout.setResizeBarTarget("next");
//		north.setHeight("0");
//		south.setHeight("0");
//		west.setWidth("0");
//		east.setWidth("0");
		
		center.setWidth("*");
		centerLayout.addComponent(west);
		centerLayout.addComponent(center);
		centerLayout.addComponent(east);
		
		addMember(north);
		addMember(centerLayout);
		addMember(south);
	}

	@Override
	public void setWidth(String width)
	{
		super.setWidth(width);
//		centerLayout.setSizeFull();
		requestRepaint();
	}

	@Override
	public void setHeight(String height)
	{
		super.setHeight(height);
		
		west.setHeight("100%");
		center.setWidth("*");
		center.setHeight("100%");
		east.setHeight("100%");
		requestRepaint();
	}


	@Override
	public void removeComponent(Component c)
	{
		replaceComponent(c, new Label(""));
	}

	/**
	 * Add component into borderlayout
	 * 
	 * @param c
	 *            component to be added into layout
	 * @param constraint
	 *            place of the component (have to be on of BorderLayout.NORTH, BorderLayout.WEST, BorderLayout.CENTER, BorderLayout.EAST, or BorderLayout.SOUTH
	 */
	public void addComponent(Component c, Constraint constraint)
	{
		if (constraint == Constraint.NORTH)
		{
			replaceComponent(north, c);
			north = c;
			if (north.getHeight() < 0 || north.getHeightUnits() == UNITS_PERCENTAGE)
			{
				north.setHeight(minimumNorthHeight);
			}
		}
		else if (constraint == Constraint.WEST)
		{
			centerLayout.replaceComponent(west, c);
			west = c;
			if (west.getWidth() < 0 || west.getWidthUnits() == UNITS_PERCENTAGE)
			{
				west.setWidth(minimumWestWidth);
			}
		}
		else if (constraint == Constraint.CENTER)
		{
			centerLayout.replaceComponent(center, c);
			center = c;
			center.setHeight(centerLayout.getHeight(), centerLayout.getHeightUnits());
			center.setWidth("100%");
		}
		else if (constraint == Constraint.EAST)
		{
			centerLayout.replaceComponent(east, c);
			east = c;
			if (east.getWidth() < 0 || east.getWidthUnits() == UNITS_PERCENTAGE)
			{
				east.setWidth(minimumEastWidth);
			}
		}
		else if (constraint == Constraint.SOUTH)
		{
			replaceComponent(south, c);
			south = c;
			if (south.getHeight() < 0 || south.getHeightUnits() == UNITS_PERCENTAGE)
			{
				south.setHeight(minimumSouthHeight);
			}
		}
		else
		{
			throw new IllegalArgumentException("Invalid BorderLayout constraint.");
		}
		requestRepaint();
	}

	@Override
	public void addComponent(Component c)
	{
		throw new IllegalArgumentException("Component constraint have to be specified");
	}

	@Override
	public void replaceComponent(Component oldComponent, Component newComponent)
	{
		if (oldComponent == north)
		{
			replaceComponent(north, newComponent);
			north = newComponent;
		}
		else if (oldComponent == west)
		{
			centerLayout.replaceComponent(west, newComponent);
			west = newComponent;
		}
		else if (oldComponent == center)
		{
			centerLayout.replaceComponent(center, newComponent);
			center = newComponent;
		}
		else if (oldComponent == east)
		{
			centerLayout.replaceComponent(east, newComponent);
			east = newComponent;
		}
		else if (oldComponent == south)
		{
			replaceComponent(south, newComponent);
			south = newComponent;
		}
		requestRepaint();
	}

	/**
	 * Set minimum height of the component in the BorderLayout.NORTH
	 * 
	 * @param minimumNorthHeight
	 */
	public void setMinimumNorthHeight(String minimumNorthHeight)
	{
		this.minimumNorthHeight = minimumNorthHeight;
	}

	/**
	 * Get minimum height of the component in the BorderLayout.NORTH
	 */
	public String getMinimumNorthHeight()
	{
		return minimumNorthHeight;
	}

	/**
	 * Set minimum height of the component in the BorderLayout.SOUTH
	 * 
	 * @param minimumNorthHeight
	 */
	public void setMinimumSouthHeight(String minimumSouthHeight)
	{
		this.minimumSouthHeight = minimumSouthHeight;
	}

	/**
	 * Get minimum height of the component in the BorderLayout.SOUTH
	 */
	public String getMinimumSouthHeight()
	{
		return minimumSouthHeight;
	}

	/**
	 * Set minimum height of the component in the BorderLayout.WEST
	 * 
	 * @param minimumNorthHeight
	 */
	public void setMinimumWestWidth(String minimumWestWidth)
	{
		this.minimumWestWidth = minimumWestWidth;
	}

	/**
	 * Get minimum height of the component in the BorderLayout.WEST
	 */
	public String getMinimumWestWidth()
	{
		return minimumWestWidth;
	}

	/**
	 * Set minimum height of the component in the BorderLayout.EAST
	 * 
	 * @param minimumNorthHeight
	 */
	public void setMinimumEastWidth(String minimumEastWidth)
	{
		this.minimumEastWidth = minimumEastWidth;
	}

	/**
	 * Get minimum height of the component in the BorderLayout.EAST
	 */
	public String getMinimumEastWidth()
	{
		return minimumEastWidth;
	}

	/**
	 * Return component from specific position
	 * 
	 * @param constraint
	 * @return
	 */
	public Component getComponent(Constraint position)
	{
		if (position == Constraint.NORTH)
		{
			return north;
		}
		else if (position == Constraint.WEST)
		{
			return west;
		}
		else if (position == Constraint.CENTER)
		{
			return center;
		}
		else if (position == Constraint.EAST)
		{
			return east;
		}
		else if (position == Constraint.SOUTH)
		{
			return south;
		}
		else
		{
			throw new IllegalArgumentException("Invalid BorderLayout constraint.");
		}
	}

	public BorderLayoutIterator<Component> getBorderLayoutComponentIterator()
	{
		return new BorderLayoutIterator<Component>(getComponentIterator(), centerLayout.getComponentIterator());
	}

	/**
	 * Iterate through the components of the borderlayout
	 * 
	 * TODO: Determine if the end user need to iterate components added into N/S/E/W locations??
	 * 
	 * @param <Component>
	 */
	@SuppressWarnings("hiding")
	private class BorderLayoutIterator<Component> implements Iterator<Component>
	{

		Iterator<Component> mainLayoutIter;
		Iterator<Component> centerLayoutIter;

		BorderLayoutIterator(Iterator<Component> mainLayoutIter, Iterator<Component> centerLayoutIter)
		{
			this.mainLayoutIter = mainLayoutIter;
			this.centerLayoutIter = centerLayoutIter;
		}

		public boolean hasNext()
		{
			return (mainLayoutIter.hasNext() || centerLayoutIter.hasNext());
		}

		public Component next()
		{
			if (mainLayoutIter.hasNext())
			{
				return mainLayoutIter.next();
			}
			else
			{
				return centerLayoutIter.next();
			}
		}

		public void remove()
		{
			if (mainLayoutIter.hasNext())
			{
				mainLayoutIter.remove();
			}
			else
			{
				centerLayoutIter.remove();
			}
		}

	}
}