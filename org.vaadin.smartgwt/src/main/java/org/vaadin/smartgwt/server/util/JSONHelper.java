package org.vaadin.smartgwt.server.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.vaadin.smartgwt.server.data.Record;

import com.google.common.collect.Maps;

public class JSONHelper {
	public static String getJsonString(Record[] records) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

		StringBuffer buffer = new StringBuffer();

		buffer.append('[');

		for (Iterator<Record> iterator = Arrays.asList(records).iterator(); iterator.hasNext();) {
			buffer.append(objectMapper.writeValueAsString(toMap(iterator.next())));

			if (iterator.hasNext()) {
				buffer.append(',');
			}
		}

		buffer.append(']');

		return buffer.toString();
	}

	private static Map toMap(Record record) throws Exception {
		final Map<Object, Object> mapped = Maps.newHashMap();
		for (String name : record.getAttributes()) {
			Object value = record.getAttributeAsObject(name);

			if (value instanceof Record) {
				mapped.put(name, toMap((Record) value));
			} else if (value instanceof Record[]) {
				Record[] arr = (Record[]) value;
				Map[] maps = new Map[arr.length];

				for (int i = 0; i < arr.length; i++) {
					maps[i] = toMap(arr[i]);
				}
				mapped.put(name, maps);
			} else {
				mapped.put(name, value);
			}
		}
		return mapped;
	}
}