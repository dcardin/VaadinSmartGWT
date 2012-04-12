package org.vaadin.smartgwt.server.util;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.vaadin.smartgwt.server.data.Record;

public class JSONHelper {
	public static String getJsonString(Record[] records) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);

		StringBuffer buffer = new StringBuffer();

		buffer.append('[');

		for (Record record : records) {
			buffer.append(objectMapper.writeValueAsString(record.getAttributes()));
			buffer.append(',');
		}

		buffer.setLength(buffer.length() - 1);
		buffer.append(']');

		return buffer.toString();
	}
}
