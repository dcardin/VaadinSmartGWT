package org.vaadin.smartgwt.server.tab;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.gwt.server.JsonPaintTarget;

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

	@Test
	public void test_noPaintToClientWhenRemovingTabOnClientCloseTabEvent() throws PaintException {
		final Tab tab = new Tab(null);
		final HashMap<String, Object> variables = Maps.newHashMap();

		tabSet.addTab(tab);
		tabSet.paintContent(mock(JsonPaintTarget.class));

		variables.put("TabCloseClickEvent.tab", tab);
		tabSet.changeVariables(null, variables);

		final JsonPaintTarget paintTarget = mock(JsonPaintTarget.class);
		tabSet.paintContent(paintTarget);

		verify(paintTarget, never()).startTag("tabs");
		verify(paintTarget, never()).addAttribute("element", tab);
	}
}
