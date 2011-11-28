package org.vaadin.smartgwt.server.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.vaadin.smartgwt.server.PropertyAccessor;

/**
 * Allows simple access / update of collections in base widgets
 * 
 * @author Daniel Cardin
 * @author NetAppsID inc.
 * 
 * @param <T>
 */
public class CollectionHandler<T> {
	private PropertyAccessor baseWidget;
	private String attributeName;
	private List<T> values = new ArrayList<T>();
	
	public CollectionHandler(String attributeName) {
		this.baseWidget = baseWidget;
		this.attributeName = attributeName;
	}

	public T[] getValues() {
		T[] values = (T[]) baseWidget.getAttributeAsObject(attributeName);

		if (values == null) {
			values = (T[]) new Object[0];
		}
		return values;
	}

	public List<T> getValuesAsList() {
		return new ArrayList<T>(Arrays.asList(getValues()));
	}

	@SuppressWarnings("unchecked")
	public void setValues(List<T> newValues) {
		T[] tmp = (T[]) Array.newInstance(Object.class, 0);
		
		T[] valuesArray = newValues.toArray(tmp);
		baseWidget.setAttribute(attributeName, valuesArray);
	}

	@SuppressWarnings("unchecked")
	public void setValues(T[] newValues) {
		baseWidget.setAttribute(attributeName, newValues);
	}

	public boolean hasValue(T value) {
		for (T t : getValues()) {
			if (t == value)
				return true;
		}

		return false;
	}

	public boolean removeValue(T valueToRemove)
	{
		List<T> values = getValuesAsList();
		
		boolean removed = values.remove(valueToRemove);
		setValues(values);
		
		return removed;
	}
	
	public void addValue(T valueToAdd)
	{
		List<T> values = getValuesAsList();
		values.add(valueToAdd);
		setValues(values);
	}
	
    public void addValue(T valueToAdd, int position) {
		List<T> values = getValuesAsList();
		values.add(position, valueToAdd);
		setValues(values);
    }
    
    public void removeValueAt(int position) {
		List<T> values = getValuesAsList();
		values.remove(position);
		setValues(values);
    }
    
    public int size()
    {
    	return getValues().length;
    }
    
    public void clear()
    {
    	baseWidget.removeAttribute(attributeName);
    }

}
