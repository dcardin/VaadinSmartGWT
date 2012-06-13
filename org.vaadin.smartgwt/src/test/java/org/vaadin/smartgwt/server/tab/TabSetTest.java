package org.vaadin.smartgwt.server.tab;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;

public class TabSetTest {
	private TabSet tabSet;

	@Before
	public void before() {
		tabSet = new TabSet();
	}

	@Test
	public void test_removesTabOnClientCloseTabEvent() {
		final Tab tab = new Tab(null);
		final HashMap<String, Object> variables = Maps.newHashMap();

		tabSet.addTab(tab);
		variables.put("TabCloseClickEvent.tab", tab);
		tabSet.changeVariables(null, variables);
		assertFalse("tab should be removed", Arrays.asList(tabSet.getTabs()).contains(tab));
	}
}
