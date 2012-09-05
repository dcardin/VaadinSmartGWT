package org.vaadin.smartgwt.client.core;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * wraps javascript native JSON library.
 */
public class JSON {
	/**
	 * converts a javascript object to it's json representation.
	 * 
	 * @param object
	 *            to be stringified.
	 * @return the generated json.
	 * @throws UnsupportedOperationException
	 *             if the browser doesn't have native support.
	 */
	public static String stringify(JavaScriptObject object) {
		if (nHasJsonStringify()) {
			return nStringify(object);
		} else {
			throw new UnsupportedOperationException("JSON.stringify is not supported in this browser.");
		}
	}

	/**
	 * converts a javascript object to it's json representation.
	 * 
	 * @param object
	 *            to be stringified.
	 * @param replacer
	 *            function.
	 * @return the generated json.
	 * @throws UnsupportedOperationException
	 *             if the browser doesn't have native support.
	 */
	public static String stringify(JavaScriptObject object, JavaScriptObject replacer) {
		if (nHasJsonStringify()) {
			return nStringify(object, replacer);
		} else {
			throw new UnsupportedOperationException("JSON.stringify is not supported in this browser.");
		}
	}

	/**
	 * creates a replacer function that will exclude keys in the exclusion list.
	 * 
	 * @param excludedKeys
	 *            to be excluded at stringification.
	 * @return the exclusion function.
	 */
	// @formatter:off
	public static native JavaScriptObject newExclusionReplacer(String[] excludedKeys) /*-{ 
		var excludedKeysJS = @com.smartgwt.client.util.JSOHelper::convertToJavaScriptArray([Ljava/lang/Object;)(excludedKeys);

		return function(key, value) { 
			for(var i = 0; i < excludedKeysJS.length; i++)
			{
				var pattern = new RegExp(excludedKeysJS[i]);
				if(pattern.exec(key)!=null)
				{
				return undefined;
				}
			}
			return value;
		};
	}-*/;
	
	private static native String nStringify(JavaScriptObject object) /*-{
																		return JSON.stringify(object);
																		}-*/;

	// @formatter:off
	private static native String nStringify(JavaScriptObject object, JavaScriptObject replacer) /*-{
																								return JSON.stringify(object, replacer);
																								}-*/;

	private static native boolean nHasJsonStringify() /*-{
														return typeof JSON == "object" && typeof JSON.stringify == "function";
														}-*/;
	// @formatter:on
}
