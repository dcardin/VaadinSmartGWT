package org.vaadin.smartgwt.server.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;
import java.util.Map.Entry;

import argo.jdom.JsonNode;
import argo.jdom.JsonStringNode;

import com.google.common.collect.Maps;

/**
 * Updates a Record with JSON information. 
 */
public class RecordJSONUpdater {
	private static interface JSONAdapter {
		void addBooleanField(String name, Boolean value);

		void addNumberField(String name, BigInteger value);

		void addNumberField(String name, BigDecimal value);

		void addStringField(String name, String value);

		void addNullField(String name);

		void addObjectField(String name, Map<String, Object> value);
	}

	/**
	 * updates the Record with information from the JsonNode. 
	 * 
	 * @param record to be updated.
	 * @param node containing update information.
	 */
	public void update(final Record record, JsonNode node) {
		update(node, new JSONAdapter() {
			@Override
			public void addBooleanField(String name, Boolean value) {
				record.setAttribute(name, value);
			}

			@Override
			public void addNumberField(String name, BigInteger value) {
				record.setAttribute(name, value.longValue());
			}

			@Override
			public void addNumberField(String name, BigDecimal value) {
				record.setAttribute(name, value.doubleValue());
			}

			@Override
			public void addStringField(String name, String value) {
				record.setAttribute(name, value);
			}

			@Override
			public void addNullField(String name) {
				record.setAttribute(name, (Object) null);
			}

			@Override
			public void addObjectField(String name, Map<String, Object> value) {
				record.setAttribute(name, value);
			}
		});
	}

	private void update(JsonNode node, JSONAdapter adapter) {
		for (Entry<JsonStringNode, JsonNode> entry : node.getFields().entrySet()) {
			final String name = entry.getKey().getText();
			if (entry.getValue().isBooleanValue()) {
				adapter.addBooleanField(name, entry.getValue().getBooleanValue());
			} else if (entry.getValue().isNumberValue()) {
				final String numberValue = entry.getValue().getNumberValue();
				if (numberValue.contains(".") || numberValue.contains("e") || numberValue.contains("E")) {
					adapter.addNumberField(name, new BigDecimal(numberValue));
				} else {
					adapter.addNumberField(name, new BigInteger(numberValue));
				}
			} else if (entry.getValue().isStringValue()) {
				adapter.addStringField(name, entry.getValue().getStringValue());
			} else if (entry.getValue().isNullNode()) {
				adapter.addNullField(name);
			} else if (entry.getValue().isObjectNode()) {
				adapter.addObjectField(name, newJsonMap(entry.getValue()));
			} else {
				throw new RuntimeException("unhandled node type " + entry.getValue());
			}
		}
	}

	private Map<String, Object> newJsonMap(JsonNode node) {
		final Map<String, Object> jso = Maps.newHashMap();
		update(node, new JSONAdapter() {
			@Override
			public void addStringField(String name, String value) {
				jso.put(name, value);
			}

			@Override
			public void addObjectField(String name, Map<String, Object> value) {
				jso.put(name, value);
			}

			@Override
			public void addNumberField(String name, BigDecimal value) {
				jso.put(name, value);
			}

			@Override
			public void addNumberField(String name, BigInteger value) {
				jso.put(name, value);
			}

			@Override
			public void addNullField(String name) {
				jso.put(name, null);
			}

			@Override
			public void addBooleanField(String name, Boolean value) {
				jso.put(name, value);
			}
		});
		return jso;
	}
}