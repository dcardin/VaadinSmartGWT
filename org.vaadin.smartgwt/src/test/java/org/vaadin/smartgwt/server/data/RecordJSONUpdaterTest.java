package org.vaadin.smartgwt.server.data;

import static org.junit.Assert.*;

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
		final JsonRootNode node = new JdomParser().parse("{ \"attribute\":true }");
		final Record record = new Record();
		updater.update(record, node);
		assertEquals(true, record.getAttributeAsBoolean("attribute"));
	}

	@Test
	public void test_updatesNumberAttribute() throws Exception {
		final JsonRootNode node = new JdomParser().parse("{ \"attribute\": 1}");
		final Record record = new Record();
		updater.update(record, node);
		assertEquals(1, (int) record.getAttributeAsInt("attribute"));
	}

	@Test
	public void test_updatesStringAttribute() throws Exception {
		final JsonRootNode node = new JdomParser().parse("{ \"attribute\":\"value\" }");
		final Record record = new Record();
		updater.update(record, node);
		assertEquals("value", record.getAttributeAsString("attribute"));
	}

	@Test(expected = RuntimeException.class)
	public void test_throwsExceptionWhenUnhandledAttributeType() throws Exception {
		final JsonRootNode node = new JdomParser().parse("{ \"attribute\":[] }");
		updater.update(new Record(), node);
	}
}
