package org.vaadin.smartgwt.server.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.vaadin.smartgwt.server.data.Record;

public class JSONHelperTest {

	@Test
	public void test_getJsonString_EmptyArrayWhenNoRecord() throws Exception {

		Record[] records = {};
		String actualResult = JSONHelper.getJsonString(records);

		assertEquals("[]", actualResult);
	}

	@Test
	public void test_getJsonString_AddedRecordWithNoAttributes() throws Exception {

		Record[] records = { new Record() };
		String actualResult = JSONHelper.getJsonString(records);

		assertEquals("[{}]", actualResult);
	}

	@Test
	public void test_getJsonString_AddedRecordWithTestAttribute() throws Exception {

		Record record = new Record();
		record.setAttribute("testAttribute", "testAttributeValue");

		Record[] records = { record };
		String actualResult = JSONHelper.getJsonString(records);

		assertEquals("[{\"testAttribute\":\"testAttributeValue\"}]", actualResult);
	}

	@Test
	public void test_getJsonString_AddedManyRecordsWithNoAttributes() throws Exception {

		Record[] records = { new Record(), new Record() };
		String actualResult = JSONHelper.getJsonString(records);

		assertEquals("[{},{}]", actualResult);
	}

	@Test
	public void test_getJsonString_AddedManyRecordsWithTestAttributes() throws Exception {

		Record record1 = new Record();
		record1.setAttribute("testAttribute1", "testAttributeValue1");

		Record record2 = new Record();
		record2.setAttribute("testAttribute2", "testAttributeValue2");

		Record[] records = { record1, record2 };
		String actualResult = JSONHelper.getJsonString(records);

		assertEquals("[{\"testAttribute1\":\"testAttributeValue1\"},{\"testAttribute2\":\"testAttributeValue2\"}]", actualResult);
	}
}
