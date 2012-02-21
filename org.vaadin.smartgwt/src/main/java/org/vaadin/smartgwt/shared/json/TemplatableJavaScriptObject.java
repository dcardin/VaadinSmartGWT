package org.vaadin.smartgwt.shared.json;

public class TemplatableJavaScriptObject extends JavaScriptObject
{
	private JavaScriptObject template = new JavaScriptObject();
	private boolean created = false;

	@Override
	public String getAsString(String attribute)
	{
		return getTarget().getAsString(attribute);
	}

	@Override
	public int getAsInt(String attribute)
	{
		return getTarget().getAsInt(attribute);
	}

	@Override
	public void put(String attribute, String value)
	{
		getTarget().put(attribute, value);
	}

	@Override
	public void put(String attribute, int value)
	{
		getTarget().put(attribute, value);
	}

	public void template(String attribute, String value)
	{
		if (!isCreated())
		{
			template.put(attribute, value);
		}
		else
		{
			throw new RuntimeException("can't set a template property once the instance is created.");
		}
	}

	public void template(String attribute, int value)
	{
		if (!isCreated())
		{
			template.put(attribute, value);
		}
		else
		{
			throw new RuntimeException("can't set a template property once the instance is created.");
		}
	}

	public boolean isCreated()
	{
		return created;
	}

	public void create()
	{
		putAllMembers(template);
		template = null;
		created = true;
	}

	private JavaScriptObject getTarget()
	{
		return isCreated() ? this : template;
	}
}
