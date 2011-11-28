package org.vaadin.smartgwt.server;

import java.util.Date;
import java.util.Map;

import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;

public interface PropertyAccessor
{

	/**
	 * Get the name of the underlying SmartClient class
	 *
	 * @return the SmartClient class name
	 */
	public abstract String getScClassName();

	/**
	 * Set the name of the underlying SmartClient class. This is an advanced setting.
	 *
	 * @param scClassName the SmartClient class
	 */
	public abstract void setScClassName(String scClassName);

	public abstract boolean isConfigOnly();

	public abstract void setConfigOnly(boolean configOnly);

	public abstract void setPosition(String position);

	public abstract String getID();

	//
	//    public void setID(String id) {
	//        if (this.id != null) {
	//            IDManager.unregisterID(this.id);
	//        }
	//        IDManager.registerID(id);
	//        setAttribute("ID", id, false);
	//        this.id = id;
	//    }

	//override default behavior of setting title for SmartGWT widgets
	public abstract void setTitle(String title);

	public abstract String getTitle();

	public abstract boolean isCreated();

	public abstract void removeAttribute(String attribute);

	public abstract void setAttribute(String attribute, Object value, boolean allowPostCreate);

	public abstract void setAttribute(String attribute, Object value);

	public abstract String getAttributeAsString(String attribute);

	public abstract String getAttribute(String attribute);

	public abstract Integer getAttributeAsInt(String attribute);

	public abstract Boolean getAttributeAsBoolean(String attribute);

	public abstract Double getAttributeAsDouble(String attribute);

	public abstract Float getAttributeAsFloat(String attribute);

	public abstract Map<?, ?> getAttributeAsMap(String attribute);

	public abstract Date getAttributeAsDate(String attribute);

	public abstract String[] getAttributeAsStringArray(String attribute);

	public abstract <T> T getAttributeAsObject(String attribute);

	public abstract void paintContent(PaintTarget target) throws PaintException;

	public abstract PropertyAccessor getOrCreateJsObj();

	public abstract PropertyAccessor getConfig();

}