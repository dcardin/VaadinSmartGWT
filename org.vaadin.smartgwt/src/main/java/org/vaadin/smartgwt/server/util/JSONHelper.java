package org.vaadin.smartgwt.server.util;

import java.util.Arrays;
import java.util.Iterator;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.vaadin.smartgwt.server.data.Record;

public class JSONHelper {
	public static String getJsonString(Record[] records) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

		StringBuffer buffer = new StringBuffer();

		buffer.append('[');

		for (Iterator<Record> iterator = Arrays.asList(records).iterator(); iterator.hasNext();) {
			buffer.append(objectMapper.writeValueAsString((iterator.next()).toMap()));

			if (iterator.hasNext()) {
				buffer.append(',');
			}
		}

		buffer.append(']');
		return buffer.toString();
	}
}
