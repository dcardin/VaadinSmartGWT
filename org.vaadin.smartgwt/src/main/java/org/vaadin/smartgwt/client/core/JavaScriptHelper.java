package org.vaadin.smartgwt.client.core;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaScriptHelper {
	//@formatter:off
	public static native String stringify(JavaScriptObject object) /*-{
		if (@org.vaadin.smartgwt.client.core.JavaScriptHelper::hasJsonStringify()()) {
			return JSON.stringify(object);
		} else {
			throw "JSON.stringify is not supported.";
		}
	}-*/;
	//@formatter:on

	//@formatter:off
	private static native boolean hasJsonStringify() /*-{
      return typeof JSON == "object" && typeof JSON.stringify == "function";
    }-*/;
	//@formatter:on
}
