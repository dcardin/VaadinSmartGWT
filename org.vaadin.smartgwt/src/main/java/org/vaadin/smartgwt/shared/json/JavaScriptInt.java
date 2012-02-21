package org.vaadin.smartgwt.shared.json;

public class JavaScriptInt implements JavaScriptValue<Integer>
{
	private final int value;

	public JavaScriptInt(int value)
	{
		this.value = value;
	}

	@Override
	public Integer getValue()
	{
		return value;
	}
}
