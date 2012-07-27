package org.vaadin.smartgwt.server.extra;

import java.util.List;
import java.util.Map;

import org.vaadin.smartgwt.server.layout.NonUIComponent;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;

/**
 * A non-visual component that notifies registered listeners at a specific interval.<p>
 * 
 * While listeners are registered, it is possible to make asynchronous UI changes without the user triggering a server call.
 */
@com.vaadin.ui.ClientWidget(org.vaadin.smartgwt.client.extra.VRefresher.class)
public class Refresher extends AbstractComponent implements NonUIComponent {
	private List<RefreshListener> listeners = Lists.newArrayList();
	private int interval;

	/**
	 * Refresher with a 1 second interval by default.
	 */
	public Refresher() {
		interval = 1000;
	}

	/**
	 * Refresh interval in milliseconds.
	 * 
	 * @return interval in milliseconds.
	 */
	public int getInterval() {
		return interval;
	}

	/**
	 * Refresh interval in milliseconds.
	 * 
	 * @param interval { _ > 0 } in milliseconds.
	 */
	public void setInterval(int interval) {
		assert interval > 0 : "interval must be a positive number";
		this.interval = interval;
	}

	/**
	 * Adds a listener that will be notified after each interval.
	 * 
	 * @param listener { _ != null } to be notified.
	 * @return the registration handler to remove the listener.
	 */
	public HandlerRegistration addListener(final RefreshListener listener) {
		assert listener != null : "listener must not be null";
		listeners.add(listener);
		requestRepaint();
		return new HandlerRegistration() {
			@Override
			public void removeHandler() {
				listeners.remove(listener);
				requestRepaint();
			}
		};
	}

	@Override
	public void paintContent(PaintTarget target) throws PaintException {
		if (!listeners.isEmpty()) {
			target.addAttribute("interval", interval);
		}
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables) {
		for (RefreshListener listener : ImmutableList.copyOf(listeners)) {
			listener.refresh(this);
		}
	}
}
