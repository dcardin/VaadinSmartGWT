package org.vaadin.smartgwt.server;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.measure.units.NonSI;
import javax.servlet.http.HttpSession;

import org.jscience.physics.measures.Measure;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedEvent;
import org.vaadin.smartgwt.server.grid.events.SelectionUpdatedHandler;
import org.vaadin.smartgwt.server.tree.PropertyGrid;
import org.vaadin.smartgwt.server.tree.Tree;
import org.vaadin.smartgwt.server.tree.TreeNode;
import org.vaadin.smartgwt.server.types.TreeModelType;

import com.jgoodies.validation.ValidationMessage;
import com.jidesoft.converter.EnumConverter;
import com.jidesoft.converter.ObjectConverter;
import com.netappsid.erp.configurator.ConfigProperty;
import com.netappsid.erp.configurator.Configurable;
import com.netappsid.erp.configurator.ConfiguratorImpl;
import com.netappsid.erp.configurator.enums.DynamicEnumSingle;
import com.netappsid.erp.configurator.utils.MeasureConverter;
import com.netappsid.utils.ReflectionUtils;
import com.netappsid.utils.StringUtils;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

public class ConfigPropertyEditor extends PropertyGrid
{
	private Map<String, Boolean> subscriptions = new HashMap<String, Boolean>();
	private Map<Integer, ConfigProperty> cps = new HashMap<Integer, ConfigProperty>();
	private int id = 1;
	private ConfiguratorImpl ci = new ConfiguratorImpl();
	private Configurable currentSelection;
	private MeasureConverter measureConverter = new MeasureConverter();

	public ConfigPropertyEditor()
	{
		addSelectionUpdatedHandler(new SelectionUpdatedHandler()
			{
				@Override
				public void onSelectionUpdated(SelectionUpdatedEvent event)
				{
					System.out.println("la selection a change sur le client: " + getSelectedRecords()[0].getAttribute("binding"));
				}
			});
	}

	/**
	 * Prepare a list of Property objects for the configurable by converting the ConfigProperty objects
	 * 
	 * @param configurable
	 *            /c
	 * @return
	 */
	private List<TreeNode> getPropertyList(Configurable configurable, String prefix, boolean onlySubscriptions)
	{
		List<TreeNode> properties = new ArrayList<TreeNode>();

		for (ConfigProperty prop : configurable.getProperties(false))
		{
			properties.addAll(getPropertyList(null, prop, prefix, onlySubscriptions));
		}

		return properties;
	}

	private List<TreeNode> getPropertyList(TreeNode parent, ConfigProperty configProperty, String prefix, boolean onlySubscriptions)
	{
		List<TreeNode> properties = new ArrayList<TreeNode>();

		if (configProperty.isVisible() == false || configProperty.isEnabled() == false)
			return properties;

		TreeNode property = null;

		// Add only properties we need on the client
		// if (!onlySubscriptions || (subscriptions.size() > 0 &&
		// subscriptions.containsKey(prefix + "." +
		// configProperty.getBindingPath())))

		if (!onlySubscriptions || (subscriptions.size() > 0 && subscriptions.containsKey(configProperty.getBindingPath())))
		{
			property = fromConfigProperty(configProperty, prefix);

			if (parent != null)
			{
				property.setAttribute("parent", parent.getAttributeAsInt("id"));
			}

			properties.add(property);
			cps.put(property.getAttributeAsInt("id"), configProperty);
		}

		if (configProperty.hasChildren())
		{

			for (ConfigProperty child : (List<ConfigProperty>) configProperty.getChildren())
			{

				List<TreeNode> childProperties = getPropertyList(property, child, prefix, onlySubscriptions);

				if (childProperties != null)
				{
					properties.addAll(childProperties);
				}
			}
		}

		return properties;
	}

	/**
	 * Convert a ConfigProperty to a Property, sent to the GWT client
	 * 
	 * @param configProperty
	 * @return
	 */
	private TreeNode fromConfigProperty(ConfigProperty configProperty, String prefix)
	{
		TreeNode property = new TreeNode();

		if (configProperty.getConverter(true) instanceof EnumConverter && configProperty.isCollectionMaster() == false) // configProperty.isEnum())
		{
			property.setAttribute("type", "enum");

			if (configProperty.getValue() != null)
			{
				Field imageField = ReflectionUtils.getDeclaredField("image", configProperty.getValue().getClass(), false);

				if (imageField != null)
				{
					try
					{
						property.setAttribute("imagePath", imageField.get(configProperty.getValue()).toString());
					}
					catch (Exception e)
					{}
				}
			}

			property.setAttribute("selections", getSelections(configProperty));
		}
		else
		{
			property.setAttribute("type", configProperty.getType().getSimpleName());
		}

		if (configProperty.isReadOnly())
			property.setAttribute("readOnly", true);
		// 
		//if (configProperty.isDynamicEnum())
		// {
		// property.setAttribute("readOnly", true);
		// }

		if (configProperty.getLabel().length() == 0)
		{
			property.setAttribute("name", configProperty.getName());
		}
		else
		{
			property.setAttribute("name", configProperty.getLabel());
		}

		property.setAttribute("value", getStringRepresentation(configProperty.getValue(), configProperty));
		property.setAttribute("id", id++);
		
		if (configProperty.isExpandable() && configProperty.isExpanded())
			property.setAttribute("expanded", true);
		
		if (getPrice(configProperty).length() > 0)
			property.setAttribute("price", getPrice(configProperty));

		if (configProperty.isOverriden())
			property.setAttribute("overridden", true);
		
		property.setAttribute("binding", configProperty.getBindingPath());

		List<TreeNode> messages = new LinkedList<TreeNode>();
		Integer severity = 0;

		for (ValidationMessage message : configProperty.getValidationMessages(false, new ArrayList<ValidationMessage>()))
		{
			TreeNode newMessage = new TreeNode();
			
			newMessage.setAttribute("message", message.formattedText());
			if (message.severity().equals(com.jgoodies.validation.Severity.ERROR))
			{
				newMessage.setAttribute("severity", 2);
				severity = 2;
			}
			else if (message.severity().equals(com.jgoodies.validation.Severity.WARNING))
			{
				newMessage.setAttribute("severity", 2);
				if (severity != 2)
					severity = 1;
			}
			else
			{
				newMessage.setAttribute("severity", 0);
			}

			messages.add(newMessage);
		}

		if (severity > 0)
			property.setAttribute("severity", severity);
		
		if (messages.size() > 0)
			property.setAttribute("messages", messages.toArray(new TreeNode[0]));

		return property;
	}

	/**
	 * Get the string representation for a property to display properly on the client. Since this is a web component, the string has to be prepared here on the
	 * server
	 * 
	 * @param value
	 * @param property
	 * @return
	 */
	private String getStringRepresentation(Object value, ConfigProperty property)
	{
		String formattedValue = ""; //$NON-NLS-1$

		ObjectConverter converter = property.getConverter(true);

		if (converter != null)
		{
			return converter.toString(value, null);
		}

		if (value != null)
		{
			if (value instanceof Measure)
			{
				Measure<?> measure = (Measure<?>) value;
				String unit = measure.getUnit().toString();
				Double dValue = measure.getEstimatedValue();
				NumberFormat nf = NumberFormat.getNumberInstance();
				nf.setMinimumFractionDigits(0);
				nf.setMaximumFractionDigits(6);

				if (measure.getUnit().equals(NonSI.INCH))
					formattedValue = StringUtils.getInchesWithFractions(dValue, 64);
				else
					formattedValue = nf.format(dValue) + " " + unit.toString(); //$NON-NLS-1$
			}
			else
			{
				formattedValue = value.toString();
			}

		}

		return formattedValue;
	}

	private String formatCurrency(double value)
	{
		// NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
		//		Currency currency = Currency.getInstance("CAD"); //$NON-NLS-1$
		// currencyFormat.setCurrency(currency);
		// String formattedValue = currencyFormat.format(value);

		DecimalFormat df = new DecimalFormat("#.00");

		// return df.format(value) + " $";
		return df.format(value) + " \u20AC";
	}

	private String getPrice(ConfigProperty property)
	{
		Double price = property.getPrice();

		if ((price != null && Math.abs(price) - 0.01 > 0) || (property.isPriceOverriden() == true))
		{
			return formatCurrency(price);
		}
		else
			return "";
	}

	public TreeNode[] getSelections(ConfigProperty cp)
	{
		if (cp == null)
		{
			return null;
		}

		EnumConverter converter = (EnumConverter) cp.getConverter(false);

		String[] labels = converter.getStrings();
		List<TreeNode> nodes = new LinkedList<TreeNode>();

		Object obj[] = converter.getObjects();

		for (int i = 0; i < labels.length; i++)
		{
			TreeNode selection = new TreeNode();
			selection.setAttribute("label", labels[i]);

			if (obj[i] != null)
			{
				Field imgField = ReflectionUtils.getDeclaredField("image", obj[i].getClass(), false);
				if (imgField != null)
				{
					imgField.setAccessible(true);
					
					try
					{
						selection.setAttribute("imagePath", imgField.get(obj[i]).toString());
					}
					catch (Exception e)
					{
						int h=0;
					}
				}
			}

			if (labels[i] != null && labels[i].equals("null") == false)
				nodes.add(selection);
		}

		TreeNode[] n = new TreeNode[nodes.size()];
		return nodes.toArray(n);
	}

	public void init(String prd)
	{ // , String variant, String locale) {
		if (prd == null)
		{
			throw new IllegalArgumentException("Product cannot be null");
		}

		try
		{
			WebApplicationContext context = (WebApplicationContext) getApplication().getContext();
			HttpSession session = context.getHttpSession();
			session.setAttribute("configurator", ci);
			session.setAttribute("initialized", true);

			ci.build(prd);
			ci.getProductMatrix().updateState(false, false);
			cps = new HashMap<Integer, ConfigProperty>();
			currentSelection = ci.getProductMatrix().getConfigurables().get(0);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		fetch();
	}

	public void fetch()
	{
		try
		{
			cps.clear();
			List<TreeNode> properties = new ArrayList<TreeNode>();

			if (currentSelection == null)
			{
				properties.addAll(getPropertyList(ci.getProductMatrix().getAssembly(), "assembly", false));
			}
			else
			{
				properties.addAll(getPropertyList((Configurable) currentSelection, "configurable", false));
			}

			setData(makeTree(properties.toArray(new TreeNode[0])));
						
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private Tree makeTree(TreeNode[] list)
	{
		Tree tree = new Tree();
		tree.setModelType(TreeModelType.PARENT);
		tree.setNameProperty("name");
		tree.setIdField("id");
		tree.setParentIdField("parent");
		tree.setShowRoot(false);
		tree.setData(list);
		return tree;
	}

	@Override
	public void changeVariables(Object source, Map<String, Object> variables)
	{
		super.changeVariables(source, variables);
		
		if (variables.containsKey("id"))
		{
			String id = variables.get("id").toString();
			Object value = variables.get("value");

			if (id != null && value != null)
			{
				ConfigProperty cp = cps.get(Integer.valueOf(id));

				if (cp != null)
				{
					update(cp, value);
				}
			}
		}
	}

	public void update(ConfigProperty cp, Object value)
	{
		String sValue = value.toString();

		if (cp.getType().getSimpleName().equalsIgnoreCase("Measure"))
		{
			value = measureConverter.getLengthMeasure(sValue, NonSI.INCH);
		}

		else if (cp.getType().getSimpleName().equalsIgnoreCase("boolean"))
		{
			value = Boolean.parseBoolean(sValue);
		}
		else if (cp.getType().getSimpleName().equalsIgnoreCase("integer"))
		{
			value = Integer.parseInt(sValue);
		}
		else if (cp.getConverter(true) instanceof EnumConverter) // cp.isEnum())
		{
			EnumConverter converter = (EnumConverter) cp.getConverter(true);
			value = converter.fromString(sValue, cp.getConverterContext());
			
			if (value instanceof DynamicEnumSingle)
			{
				DynamicEnumSingle des = (DynamicEnumSingle) value;
			}
		}

		cp.setValue(value);
		ci.getProductMatrix().updateState(false, false);
		fetch();
	}

}
