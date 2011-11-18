package org.vaadin.smartgwt.server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import com.vaadin.terminal.Paintable;

/**
 * <p>
 * This class contains the logic necessary for those UI components which needs to perform partial server-client updates. The component needs to have this as a
 * private field and implement calls at key points (see all methods' javadocs).
 * </p>
 * <p>
 * Creditation: the main idea is obtained from http://code.google.com/p/vopenlayers/source/browse/trunk/src/main/java/org/vaadin/vol/OpenLayersMap.java
 * </p>
 * 
 * @since 0.2.9
 * @author ttran at coolersport.info
 **/
public class PartialPaintChecker implements Serializable
{
	private HashMap<String, Boolean> flags = new HashMap<String, Boolean>();
	private boolean partialRepaint = true;
	private boolean fullRepaint = true;
	private Paintable p;
	private static boolean forcedFullRepaint = false;

	public static void forceFullRepaint()
	{
		forcedFullRepaint = true;
	}

	public static void resetFullRepaint()
	{
		forcedFullRepaint = false;
	}

	public Set<String> getFlagged()
	{
		return flags.keySet();
	}

	/**
	 * <p>
	 * Construct the checker only for this paintable.
	 * </p>
	 * 
	 * @param paintable
	 *            dedicated paintable
	 **/
	public PartialPaintChecker(final Paintable paintable)
	{
		p = paintable;
	}

	/**
	 * <p>
	 * Check if a partial repaint is needed instead of full repaint.
	 * </p>
	 * 
	 * @return false if full repaint is required or no flag was marked as dirty, true otherwise
	 **/
	public boolean isPartialRepaint()
	{
		if (fullRepaint || forcedFullRepaint)
			return false;
		return flags.size() > 0;
	}

	/**
	 * <p>
	 * Check if a full repaint is needed.
	 * </p>
	 * 
	 * @return true if full repaint is required
	 **/
	public boolean isFullRepaint()
	{
		return fullRepaint || forcedFullRepaint;
	}

	/**
	 * <p>
	 * This method is meant to be called inside overridden paintContent() method to determine if a flag is marked as dirty to send relevant update to client.
	 * Developer can rely on this method to decide when to send updates for relevant data.
	 * </p>
	 * 
	 * @param flag
	 *            flag to check
	 * @return false if the flag was not marked as dirty, true otherwise (i.e. full repaint is needed or no flag was marked as dirty)
	 **/
	public boolean isDirty(final String flag)
	{
		return (fullRepaint || forcedFullRepaint || flags.containsKey(flag));
	}

	/**
	 * <p>
	 * Indicate if a flag is dirty and needs to do a partial repaint. If a full repaint is not in progress, {@link #partialPaint()} will be triggered and
	 * eventually call {@link Paintable#requestRepaint()}.
	 * </p>
	 * <p>
	 * Developer may explicitly call {@link Paintable#requestRepaint()} after calling this method to enforce a full repaint.
	 * </p>
	 * 
	 * @param flag
	 *            flag to set as dirty, flags are stored using bit-comparison so ensure its value is a binary-bit like: 1, 2, 4, 8, 16, 32, and so on
	 **/
	public void setDirty(final String attribute)
	{
		if (!fullRepaint)
		{
			flags.put(attribute, true);
			partialPaint();
		}
	}

	/**
	 * <p>
	 * The paintable must override paintContent() method and call this method at very end of the method.
	 * </p>
	 **/
	public void paintContentPerformed()
	{
		clearDirtyFlags();
		fullRepaint = false;
	}

	/**
	 * <p>
	 * The paintable must override {@link Paintable#requestRepaint()} method and call this method at very beginning of the method.
	 * </p>
	 **/
	public void checkBeforeRequestRepaint()
	{
		if (!partialRepaint)
		{
			clearDirtyFlags();
			fullRepaint = true;
		}
	}

	private void clearDirtyFlags()
	{
		flags.clear();
	}

	private void partialPaint()
	{
		partialRepaint = true;
		try
		{
			p.requestRepaint();
		}
		finally
		{
			partialRepaint = false;
		}
	}
}