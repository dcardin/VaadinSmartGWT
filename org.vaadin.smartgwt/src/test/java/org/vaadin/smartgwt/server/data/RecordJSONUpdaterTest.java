package org.vaadin.smartgwt.server.data;

import static argo.jdom.JsonNodeFactories.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;

public class RecordJSONUpdaterTest {
	private RecordJSONUpdater updater;

	@Before
	public void before() {
		updater = new RecordJSONUpdater();
	}

	@Test
	public void test_updatesBooleanAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonTrue()));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals(true, record.getAttributeAsBoolean("attribute"));
	}

	@Test
	public void test_updatesNumberAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonNumber(new BigInteger("10"))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals(new Long(new BigInteger("10").longValue()), record.getAttributeAsLong("attribute"));
	}

	@Test
	public void test_updatesDecimalNumberAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonNumber(new BigDecimal("10.10"))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals(new Double(new BigDecimal("10.10").doubleValue()), record.getAttributeAsDouble("attribute"));
	}

	@Test
	public void test_updatesExponentNumberAttribute_e() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonNumber(new BigDecimal("1e2"))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals(new Double(new BigDecimal("1e2").doubleValue()), record.getAttributeAsDouble("attribute"));
	}

	@Test
	public void test_updatesExponentNumberAttribute_E() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonNumber(new BigDecimal("1E2"))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals(new Double(new BigDecimal("1E2").doubleValue()), record.getAttributeAsDouble("attribute"));
	}

	@Test
	public void test_updatesExponentAndDecimalNumberAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonNumber(new BigDecimal("1.1e2"))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals(new Double(new BigDecimal("1.1e2").doubleValue()), record.getAttributeAsDouble("attribute"));
	}

	@Test
	public void test_updatesStringAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonString("value")));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals("value", record.getAttributeAsString("attribute"));
	}

	@Test
	public void test_updatesNullAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonNull()));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertNull(record.getAttributeAsObject("attribute"));
	}

	@Test
	public void test_updatesJSONObjectAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonObject(
					aJsonField("attribute1", aJsonString("value1")),
					aJsonField("attribute2", aJsonString("value2")))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals("value1", record.getAttributeAsMap("attribute").get("attribute1"));
		assertEquals("value2", record.getAttributeAsMap("attribute").get("attribute2"));
	}

	@Test
	public void test_updatesJSONObjectNestedAttribute() throws Exception {
		//@formatter:off
		final JsonRootNode node = aJsonObject(
			aJsonField("attribute", aJsonObject(
					aJsonField("attribute", aJsonObject(
						aJsonField("attribute", aJsonString("value")))))));
		//@formatter:on

		final Record record = new Record();
		updater.update(record, node);
		assertEquals("value", ((Map<String, Object>) record.getAttributeAsMap("attribute").get("attribute")).get("attribute"));
	}

	@Test(expected = RuntimeException.class)
	public void test_throwsExceptionWhenUnhandledAttributeType() throws Exception {
		final JsonRootNode node = new JdomParser().parse("{ \"attribute\":[] }");
		updater.update(new Record(), node);
	}
}
