package org.vaadin.smartgwt.client.core;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * wraps javascript native JSON library.
 */
public class JSON {
	/**
	 * converts a javascript object to it's json representation.
	 * 
	 * @param object to be stringified.
	 * @return the generated json.
	 * @throws UnsupportedOperationException if the browser doesn't have native support.
	 */
	public static String stringify(JavaScriptObject object) {
		if (nHasJsonStringify()) {
			return nStringify(object);
		} else {
			throw new UnsupportedOperationException("JSON.stringify is not supported in this browser.");
		}
	}

	//@formatter:off
	private static native String nStringify(JavaScriptObject object) /*-{
		return JSON.stringify(object);
	}-*/;

	private static native boolean nHasJsonStringify() /*-{
      return typeof JSON == "object" && typeof JSON.stringify == "function";
    }-*/;
	//@formatter:on
}
