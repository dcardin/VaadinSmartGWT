package org.vaadin.smartgwt.shared.json;

public class JavaScriptString implements JavaScriptValue<String>
{
	private final String value;

	public JavaScriptString(String value)
	{
		this.value = value;
	}

	@Override
	public String getValue()
	{
		return value;
	}
}
